//import java.io.*;
//import java.util.*;
//import java.awt.Point;

/**
 * Klasa zarządzająca światem gry w jednym pliku.
 */
/*
public class Swiat {
    private int planszaX = 20;
    private int planszaY = 20;
    private int licznikTur = 0;
    private List<String> logBuffer = new ArrayList<>();

    private List<Organizm> organizmy = new ArrayList<>();
    private char lastInput;

    public void wykonajTure(char input) {
        clearScreen();
        lastInput = input;
        // reset akcji i zapamiętanie pozycji
        for (Organizm org : organizmy) {
            org.ustawWykonanoAkcje(false);
            org.zapiszPozycje();
        }
        // sortowanie
        organizmy.sort((a, b) -> {
            if (a.getInicjatywa() == b.getInicjatywa())
                return Integer.compare(b.getWiek(), a.getWiek());
            return Integer.compare(b.getInicjatywa(), a.getInicjatywa());
        });
        // wykonanie akcji i kolizji
        int size = organizmy.size();
        for (int i = 0; i < size; i++) {
            Organizm org = organizmy.get(i);
            if (!org.czyZyje() || org.czyWykonanoAkcje()) continue;
            org.akcja();
            org.ustawWykonanoAkcje(true);
            for (int j = 0; j < organizmy.size(); j++) {
                if (i == j) continue;
                Organizm other = organizmy.get(j);
                if (!other.czyZyje()) continue;
                if (other.getX() == org.getX() && other.getY() == org.getY()) {
                    other.kolizja(org);
                    break;
                }
            }
        }
        // usuwanie martwych
        usunMartweOrganizmy();
        // zwiększanie wieku
        for (Organizm org : organizmy) if (org.czyZyje()) org.zwiekszWiek();
        licznikTur++;

    }

    public char getLastInput() {
        return lastInput;
    }

    public int getFreeX() {
        Random rnd = new Random();
        int x;
        boolean hasFree;
        do {
            x = rnd.nextInt(planszaX);
            int finalX = x;
            hasFree = organizmy.stream().filter(o -> o.getX() == finalX).count() < planszaY;
        } while (!hasFree);
        return x;
    }

    public int getFreeY(int x) {
        Random rnd = new Random();
        int y;
        do {
            y = rnd.nextInt(planszaY);
        } while (getOrganizmNaPolu(x, y) != null);
        return y;
    }

    public void rysujSwiat() {
        for (int i = 0; i < planszaX; i++) {
            for (int j = 0; j < planszaY; j++) {
                Organizm org = getOrganizmNaPolu(i, j);
                char symbol = (org != null) ? org.rysowanie() : ' ';
                System.out.print("[" + symbol + "]");
            }
            System.out.println();
        }
        System.out.println("Hubert Siemaszko 202413");
    }

    public void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.out.println("--- Nowa tura ---");
        }
    }

    public void dodajOrganizm(Organizm org) {
        organizmy.add(org);
    }

    public List<Organizm> getOrganizmy() {
        return organizmy;
    }

    public void usunOrganizm(Organizm org) {
        org.setZyje(false);
    }

    public Organizm getOrganizmNaPolu(int x, int y) {
        return organizmy.stream()
                .filter(o -> o.getX() == x && o.getY() == y)
                .findFirst().orElse(null);
    }

    public Point znajdzPusteObok(int x, int y) {
        int[] dx = {1,1,1,0,0,-1,-1,-1};
        int[] dy = {1,0,-1,1,-1,1,0,-1};
        for (int k = 0; k < dx.length; k++) {
            int nx = x + dx[k], ny = y + dy[k];
            if (nx >= 0 && nx < planszaX && ny >= 0 && ny < planszaY && getOrganizmNaPolu(nx, ny)==null) {
                return new Point(nx, ny);
            }
        }
        return new Point(-1, -1);
    }

    public void dodajDoDziennika(String tekst) {
        logBuffer.add(tekst);
    }

    public List<String> pobierzLogi() {
        List<String> copy = new ArrayList<>(logBuffer);
        logBuffer.clear();
        return copy;
    }

    public void usunMartweOrganizmy() {
        organizmy.removeIf(o -> !o.czyZyje());
    }

    public boolean zapiszDoPliku(String nazwaPliku) {
        try (PrintWriter out = new PrintWriter(nazwaPliku)) {
            out.printf("SWIAT;%d;%d;%d%n", planszaX, planszaY, licznikTur);
            for (Organizm o : organizmy) {
                out.print(o.getNazwa() + ";"
                        + o.getSila() + ";"
                        + o.getInicjatywa() + ";"
                        + o.getWiek() + ";"
                        + o.getX() + ";"
                        + o.getY());
                if (o instanceof Czlowiek) {
                    Czlowiek cz = (Czlowiek)o;
                    out.print(";" + cz.getActiveTurns() + ";" + cz.getCooldown());
                }
                out.println();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean wczytajZPliku(String nazwaPliku) {
        try (BufferedReader in = new BufferedReader(new FileReader(nazwaPliku))) {
            organizmy.clear();
            String line = in.readLine();
            if (line == null) return false;
            String[] hdr = line.split(";");
            planszaX = Integer.parseInt(hdr[1]);
            planszaY = Integer.parseInt(hdr[2]);
            licznikTur = Integer.parseInt(hdr[3]);
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(";");
                String typ = parts[0];
                int sila = Integer.parseInt(parts[1]);
                int inicjatywa = Integer.parseInt(parts[2]);
                int wiek = Integer.parseInt(parts[3]);
                int x = Integer.parseInt(parts[4]);
                int y = Integer.parseInt(parts[5]);
                Organizm nowy = null;
                switch (typ) {
                    case "Trawa": nowy = new Trawa(sila, inicjatywa, x,y, this, "T"); break;
                    case "Wilk": nowy = new Wilk(sila, inicjatywa, x, y, this, "W"); break;
                    case "Owca": nowy = new Owca(sila, inicjatywa,  x, y, this, "O"); break;
                    case "Żółw": nowy = new Żółw(sila, inicjatywa,  x, y, this,"Z"); break;
                    case "Mlecz": nowy = new Mlecz(sila, inicjatywa, x, y, this, "M"); break;
                    case "Guarana": nowy = new Guarana(sila, inicjatywa, x, y, this, "G"); break;
                    case "WilczeJagody": nowy = new WilczeJagody(sila, inicjatywa, x, y, this, "J"); break;
                    case "Lis": nowy = new Lis(sila, inicjatywa,  x, y, this, "L"); break;
                    case "Antylopa": nowy = new Antylopa(sila, inicjatywa,  x, y, this, "A"); break;
                    case "BarszczSosnowskiego": nowy = new BarszczSosnowskiego(sila, inicjatywa,  x, y, this, "B"); break;
                    case "Cyberowca": nowy = new Cyberowca(sila, inicjatywa,  x, y, this,"C"); break;
                    case "Czlowiek": {
                        int active = Integer.parseInt(parts[6]);
                        int cooldown = Integer.parseInt(parts[7]);
                        Czlowiek cz = new Czlowiek(sila, inicjatywa,  x, y, this, "H");
                        cz.setActiveTurns(active);
                        cz.setCooldown(cooldown);
                        nowy = cz;
                        break;
                    }
                }
                if (nowy != null) {
                    nowy.setWiek(wiek);
                    organizmy.add(nowy);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public int getPlanszaX() { return planszaX; }
    public int getPlanszaY() { return planszaY; }
}
*/
