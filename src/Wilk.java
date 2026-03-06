import java.awt.*;
import java.util.List;

public class Wilk extends Zwierze {
    private final String znak;

    public Wilk(int sila, int inicjatywa, int x, int y, SwiatInterface swiat, String znak) {
        super(sila, inicjatywa, x, y,swiat);
        this.znak = znak;
    }

    @Override
    public void akcja() {
        int oldX = this.x;
        int oldY = this.y;
        super.akcja();
        String log = "Wilk przesunal sie z miejsca (" + oldX + "," + oldY + ") na miejsce (" + x + "," + y + ")";
        swiat.dodajDoDziennika(log);
    }

    @Override
    public Zwierze stworzDziecko(int x, int y) {
        return new Wilk(9, 5, x, y, swiat, znak);
    }

    @Override
    public String getNazwa() {
        return "Wilk";
    }

    @Override
    public char rysowanie() {
        return znak.charAt(0);
    }

    @Override
    public boolean czyOdbilAtak(Organizm atakujacy) {
        return false;
    }

    @Override
    public void kolizja(Organizm inny) {
        if (czyOdbilAtak(inny)) {
            return;
        }
        super.kolizja(inny);
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
