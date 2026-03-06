
import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SwiatHex implements SwiatInterface {
    private int planszaX = 10;
    private int planszaY = 10;
    private int licznikTur = 0;
    private final List<String> logBuffer = new ArrayList<>();
    private final List<Organizm> organizmy = new ArrayList<>();
    private char lastInput;

    public SwiatHex() { }

    public SwiatHex(int planszaX, int planszaY) {
        this.planszaX = planszaX;
        this.planszaY = planszaY;
    }

    @Override
    public void wykonajTure(char input) {
        lastInput = input;
        for (Organizm o : organizmy) {
            o.ustawWykonanoAkcje(false);
            o.zapiszPozycje();
        }
        organizmy.sort((a, b) -> {
            if (a.getInicjatywa() == b.getInicjatywa())
                return Integer.compare(b.getWiek(), a.getWiek());
            return Integer.compare(b.getInicjatywa(), a.getInicjatywa());
        });
        int n = organizmy.size();
        for (int i = 0; i < n; i++) {
            Organizm o = organizmy.get(i);
            if (!o.czyZyje() || o.czyWykonanoAkcje()) continue;
            o.akcja();
            o.ustawWykonanoAkcje(true);
            for (int j = 0; j < organizmy.size(); j++) {
                if (i == j) continue;
                Organizm other = organizmy.get(j);
                if (!other.czyZyje()) continue;
                if (other.getX() == o.getX() && other.getY() == o.getY()) {
                    other.kolizja(o);
                    break;
                }
            }
        }
        usunMartweOrganizmy();
        for (Organizm o : organizmy) if (o.czyZyje()) o.zwiekszWiek();
        licznikTur++;
    }

    @Override public char getLastInput()               { return lastInput; }
    @Override public int getPlanszaX()                 { return planszaX; }
    @Override public int getPlanszaY()                 { return planszaY; }

    @Override
    public int getFreeX() {
        Random rnd = new Random();
        int x;
        boolean ok;
        do {
            x = rnd.nextInt(planszaX);
            int finalX = x;
            long cnt = organizmy.stream().filter(o -> o.getX() == finalX).count();
            ok = cnt < planszaY;
        } while (!ok);
        return x;
    }

    @Override
    public int getFreeY(int x) {
        Random rnd = new Random();
        int y;
        do {
            y = rnd.nextInt(planszaY);
        } while (getOrganizmNaPolu(x, y) != null);
        return y;
    }

    @Override public void dodajOrganizm(Organizm o)     { organizmy.add(o); }
    @Override public List<Organizm> getOrganizmy()      { return organizmy; }
    @Override public void usunOrganizm(Organizm o)      { o.setZyje(false); }

    @Override
    public Organizm getOrganizmNaPolu(int x, int y) {
        return organizmy.stream()
                .filter(o -> o.getX() == x && o.getY() == y)
                .findFirst().orElse(null);
    }

    @Override
    public Point znajdzPusteObok(int x, int y) {
        int[][] evenDirs = {{+1,0},{0,-1},{-1,0},{-1,+1},{0,+1},{+1,+1}};
        int[][] oddDirs  = {{+1,-1},{0,-1},{-1,-1},{-1,0},{0,+1},{+1,0}};
        int[][] dirs = (y % 2 == 0) ? evenDirs : oddDirs;
        for (int[] d : dirs) {
            int nx = x + d[0], ny = y + d[1];
            if (nx >= 0 && nx < planszaX && ny >= 0 && ny < planszaY
                    && getOrganizmNaPolu(nx, ny) == null) {
                return new Point(nx, ny);
            }
        }
        return new Point(-1, -1);
    }

    @Override public void dodajDoDziennika(String txt)  { logBuffer.add(txt); }

    @Override
    public List<String> pobierzLogi() {
        List<String> out = new ArrayList<>(logBuffer);
        logBuffer.clear();
        return out;
    }

    @Override
    public boolean zapiszDoPliku(String fname) {
        try (PrintWriter out = new PrintWriter(fname)) {


            out.printf("HEX;%d;%d;%d%n", planszaX, planszaY, licznikTur);
            for (Organizm o : organizmy) {
                out.print(o.getNazwa() + ";"
                        + o.getSila() + ";" + o.getInicjatywa() + ";"
                        + o.getWiek() + ";" + o.getX() + ";" + o.getY());
                if (o instanceof Czlowiek) {
                    Czlowiek c = (Czlowiek)o;
                    out.print(";" + c.getActiveTurns() + ";" + c.getCooldown());
                }
                out.println();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean wczytajZPliku(String fname) {
        try (BufferedReader in = new BufferedReader(new FileReader(fname))) {
            organizmy.clear();
            String header = in.readLine();
            if (header == null) return false;
            String[] h = header.split(";");
            if (!"HEX".equals(h[0])) return false;

            planszaX = Integer.parseInt(h[1]);
            planszaY = Integer.parseInt(h[2]);
            licznikTur = Integer.parseInt(h[3]);
            String line;
            while ((line = in.readLine()) != null) {
                String[] p = line.split(";");
                String typ = p[0];
                int sila = Integer.parseInt(p[1]);
                int inic = Integer.parseInt(p[2]);
                int wiek = Integer.parseInt(p[3]);
                int x    = Integer.parseInt(p[4]);
                int y    = Integer.parseInt(p[5]);
                Organizm nowy = null;
                switch (typ) {
                    case "Trawa": nowy = new Trawa(sila,inic,x,y,this,"T"); break;
                    case "Wilk": nowy = new Wilk(sila,inic,x,y,this,"W"); break;
                    case "Owca": nowy = new Owca(sila,inic,x,y,this,"O"); break;
                    case "Żółw": nowy = new Żółw(sila,inic,x,y,this,"Z"); break;
                    case "Mlecz": nowy = new Mlecz(sila,inic,x,y,this,"M"); break;
                    case "Guarana": nowy = new Guarana(sila,inic,x,y,this,"G"); break;
                    case "WilczeJagody": nowy = new WilczeJagody(sila,inic,x,y,this,"J"); break;
                    case "Lis": nowy = new Lis(sila,inic,x,y,this,"L"); break;
                    case "Antylopa": nowy = new Antylopa(sila,inic,x,y,this,"A"); break;
                    case "BarszczSosnowskiego":
                        nowy = new BarszczSosnowskiego(sila,inic,x,y,this,"B"); break;
                    case "Cyberowca":
                        nowy = new Cyberowca(sila,inic,x,y,this,"C"); break;
                    case "Czlowiek": {
                        int act = Integer.parseInt(p[6]);
                        int cd  = Integer.parseInt(p[7]);
                        Czlowiek c = new Czlowiek(sila,inic,x,y,this,"H");
                        c.setActiveTurns(act);
                        c.setCooldown(cd);
                        nowy = c;
                        break;
                    }
                }
                if (nowy != null) {
                    nowy.setWiek(wiek);
                    organizmy.add(nowy);
                }
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    private void usunMartweOrganizmy() {
        organizmy.removeIf(o -> !o.czyZyje());
    }
}
