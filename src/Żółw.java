import java.awt.Point;
import java.util.List;
import java.util.Random;


public class Żółw extends Zwierze {
    private final String znak;

    public Żółw(int sila, int inicjatywa, int x, int y, SwiatInterface swiat, String znak) {
        super(sila, inicjatywa, x, y,swiat);
        this.znak = znak;
    }

    @Override public boolean czyOdbilAtak(Organizm atakujacy) {
        if (atakujacy.getSila() < 5) {
            atakujacy.przywrocPoprzedniaPozycje();
            swiat.dodajDoDziennika("Atakujący nie zadał obrażeń żółwiowi");
            return true;
        }
        return false;
    }

    @Override
    public void akcja() {
        int chance = new Random().nextInt(4);
        int oldX = getX();
        int oldY = getY();
        if (chance == 1) {
            super.akcja();
        }
        swiat.dodajDoDziennika(
                "Żółw przesunął się z miejsca (" + oldX + "," + oldY + ") na miejsce (" + getX() + "," + getY() + ")"
        );
    }

    @Override
    public Zwierze stworzDziecko(int x, int y) {
        return new Żółw(2, 1, x, y, swiat, znak);
    }

    @Override
    public String getNazwa() {
        return "Żółw";
    }

    @Override
    public char rysowanie() {
        return znak.charAt(0);
    }

    @Override
    public void kolizja(Organizm inny) {
        if (inny == null) return;
        if (inny.getX() != getX() || inny.getY() != getY()) return;

        if (inny instanceof Zwierze) {
            if (czyOdbilAtak(inny)) {
                return;
            }
        }

        if (inny instanceof Żółw) {
            Point free = swiat.znajdzPusteObok(getX(), getY());
            if (free.x >= 0) {
                Zwierze dziecko = stworzDziecko(free.x, free.y);
                dziecko.ustawWykonanoAkcje(true);
                swiat.dodajOrganizm(dziecko);
                swiat.dodajDoDziennika(
                        "Żółw rozmnożył się na (" + free.x + "," + free.y + ")"
                );
                ustawWykonanoAkcje(true);
                inny.ustawWykonanoAkcje(true);
            }
            return;
        }

        if (getSila() > inny.getSila()) {
            swiat.dodajDoDziennika("Żółw zabił " + inny.getNazwa());
            swiat.usunOrganizm(inny);
        } else {
            swiat.dodajDoDziennika(inny.getNazwa() + " zabił żółwia");
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