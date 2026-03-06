
import java.awt.Point;
import java.util.List;


public class BarszczSosnowskiego extends Roślina {
    private final String znak;

    public BarszczSosnowskiego(int sila, int inicjatywa, int x, int y, SwiatInterface swiat, String znak) {
        super(sila, inicjatywa, x, y, swiat);
        this.znak = znak;
    }

    @Override

    public void akcja() {
        if (czyWykonanoAkcje()) return;
        zapiszPozycje();

        boolean hex = swiat instanceof SwiatHex;
        if (hex) {
            int[][] evenDirs = {{+1,0},{0,-1},{-1,0},{-1,+1},{0,+1},{+1,+1}};
            int[][] oddDirs  = {{+1,-1},{0,-1},{-1,-1},{-1,0},{0,+1},{+1,0}};
            int[][] dirs = (getY() % 2 == 0) ? evenDirs : oddDirs;

            for (int[] dir : dirs) {
                int nx = getX() + dir[0];
                int ny = getY() + dir[1];
                if (nx < 0 || nx >= swiat.getPlanszaX() || ny < 0 || ny >= swiat.getPlanszaY()) continue;
                Organizm o = swiat.getOrganizmNaPolu(nx, ny);
                if (o instanceof Zwierze && !(o instanceof Cyberowca)) {
                    swiat.dodajDoDziennika(
                            getNazwa() + " zabił(a) " + o.getNazwa() + " na (" + nx + "," + ny + ")"
                    );
                    swiat.usunOrganizm(o);
                }
            }
        } else {
            int[] DX = {-1, -1, -1, 0, 0, 1, 1, 1};
            int[] DY = {-1, 0, 1, -1, 1, -1, 0, 1};
            for (int k = 0; k < DX.length; k++) {
                int nx = getX() + DX[k];
                int ny = getY() + DY[k];
                if (nx < 0 || nx >= swiat.getPlanszaX() || ny < 0 || ny >= swiat.getPlanszaY()) continue;
                Organizm o = swiat.getOrganizmNaPolu(nx, ny);
                if (o instanceof Zwierze && !(o instanceof Cyberowca)) {
                    swiat.dodajDoDziennika(
                            getNazwa() + " zabił(a) " + o.getNazwa() + " na (" + nx + "," + ny + ")"
                    );
                    swiat.usunOrganizm(o);
                }
            }
        }

        super.akcja();
        ustawWykonanoAkcje(true);
    }


    @Override
    public Roślina stworzDziecko(int x, int y) {
        return new BarszczSosnowskiego(10, 0, x, y, swiat, "B");
    }

    @Override
    public String getNazwa() {
        return "BarszczSosnowskiego";
    }

    @Override
    public char rysowanie() {
        return znak.charAt(0);
    }

    @Override
    public void kolizja(Organizm inny) {
        if (inny != null) {
            if (!(inny instanceof Cyberowca)) {
                swiat.usunOrganizm(inny);
            } else {
                swiat.dodajDoDziennika("Cyberowca zjadł Barszcz Sosnowskiego");
            }
            this.setZyje(false);
            swiat.usunOrganizm(this);
        }
    }

    @Override
    public void wykonajTure(char input) {

    }

    @Override
    public char getLastInput() {
        return 0;
    }

    @Override
    public int getPlanszaX() {
        return 0;
    }

    @Override
    public int getPlanszaY() {
        return 0;
    }

    @Override
    public int getFreeX() {
        return 0;
    }

    @Override
    public int getFreeY(int x) {
        return 0;
    }

    @Override
    public void dodajOrganizm(Organizm org) {

    }

    @Override
    public List<Organizm> getOrganizmy() {
        return List.of();
    }

    @Override
    public void usunOrganizm(Organizm org) {

    }

    @Override
    public Organizm getOrganizmNaPolu(int x, int y) {
        return null;
    }

    @Override
    public Point znajdzPusteObok(int x, int y) {
        return null;
    }

    @Override
    public void dodajDoDziennika(String tekst) {

    }

    @Override
    public List<String> pobierzLogi() {
        return List.of();
    }

    @Override
    public boolean zapiszDoPliku(String nazwaPliku) {
        return false;
    }

    @Override
    public boolean wczytajZPliku(String nazwaPliku) {
        return false;
    }
}
