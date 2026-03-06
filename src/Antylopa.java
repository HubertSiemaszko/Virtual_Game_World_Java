import java.awt.Point;
import java.util.List;
import java.util.Random;

public class Antylopa extends Zwierze {
    private final String znak;

    public Antylopa(int sila, int inicjatywa, int x, int y, SwiatInterface swiat, String znak) {
        super(sila, inicjatywa, x, y, swiat);
        this.znak = znak;
    }

    @Override
    public boolean czyOdbilAtak(Organizm atakujacy) {
        return false;
    }

    @Override
    public void akcja() {
        zapiszPozycje();
        boolean hex = swiat instanceof SwiatHex;
        Random rnd = new Random();
        int newX = getX();
        int newY = getY();

        if (hex) {
            int[][] evenDirs = {{+2,0},{1,-2},{-1,-2},{-2,0},{-1,+2},{1,+2}};
            int[][] oddDirs  = {{+1,-2},{-1,-2},{-2,0},{-1,+2},{1,+2},{2,0}};
            int[][] dirs = (getY() % 2 == 0) ? evenDirs : oddDirs;
            int i = rnd.nextInt(dirs.length);
            newX = getX() + dirs[i][0];
            newY = getY() + dirs[i][1];
        } else {
            int[] DX = {-2, 0, 2, -2, 2, -2, 0, 2};
            int[] DY = {-2, -2, -2, 0, 0, 2, 2, 2};
            int i = rnd.nextInt(DX.length);
            newX = getX() + DX[i];
            newY = getY() + DY[i];
        }

        if (newX >= 0 && newX < swiat.getPlanszaX() && newY >= 0 && newY < swiat.getPlanszaY()) {
            int oldX = getX(), oldY = getY();
            setPozycja(newX, newY);
            swiat.dodajDoDziennika(
                    "Antylopa przesunęła się z miejsca (" + oldX + "," + oldY + ") na miejsce (" + newX + "," + newY + ")"
            );
        }
    }

    @Override
    public Zwierze stworzDziecko(int x, int y) {
        return new Antylopa(4, 4, x, y, swiat, znak);
    }

    @Override
    public String getNazwa() {
        return "Antylopa";
    }

    @Override
    public char rysowanie() {
        return znak.charAt(0);
    }

    @Override
    public void kolizja(Organizm inny) {
        if (inny == null) return;
        if (inny.getX() != getX() || inny.getY() != getY()) return;

        if (inny instanceof Antylopa) {
            Point free = swiat.znajdzPusteObok(getX(), getY());
            if (free.x >= 0 && free.y >= 0) {
                Zwierze dziecko = stworzDziecko(free.x, free.y);
                swiat.dodajOrganizm(dziecko);
                swiat.dodajDoDziennika("Rodzi się nowa Antylopa.");
                ustawWykonanoAkcje(true);
                inny.ustawWykonanoAkcje(true);
            }
            return;
        }

        if (new Random().nextBoolean()) {
            Point free = swiat.znajdzPusteObok(getX(), getY());
            if (free.x >= 0 && free.y >= 0) {
                setPozycja(free.x, free.y);
                swiat.dodajDoDziennika(
                        "Antylopa uciekła na pole (" + free.x + "," + free.y + ")"
                );
                return;
            } else {
                swiat.dodajDoDziennika("Antylopa nie mogła uciec, brak wolnego pola.");
            }
        }

        if (getSila() > inny.getSila()) {
            swiat.dodajDoDziennika("Antylopa zjadła " + inny.getNazwa());
            swiat.usunOrganizm(inny);
        } else {
            swiat.dodajDoDziennika(inny.getNazwa() + " zjadł Antylopę");
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