import java.awt.*;
import java.util.List;

public class Mlecz extends Roślina {
    private final String znak;

    public Mlecz(int sila, int inicjatywa, int x, int y, SwiatInterface swiat, String znak) {
        super(sila, inicjatywa, x, y, swiat);
        this.znak = znak;
    }

    @Override
    public void akcja() {
        if (czyWykonanoAkcje()) return;

        for (int i = 0; i < 3; i++) {
            super.akcja();
        }

        ustawWykonanoAkcje(true);
    }

    @Override
    public Roślina stworzDziecko(int x, int y) {
        return new Mlecz(0, 0, x, y, swiat, "M");
    }

    @Override
    public char rysowanie() {
        return znak.charAt(0);
    }

    @Override
    public void kolizja(Organizm inny) {
        if (inny == null) {
            System.out.println("Błąd: wskaźnik 'inny' jest null.");
            return;
        }

        if (inny.getX() == this.getX() && inny.getY() == this.getY()) {
            if (this.getSila() > inny.getSila()) {
                String log = "MLECZ ZABIŁ " + inny.getNazwa();
                swiat.dodajDoDziennika(log);
                swiat.usunOrganizm(inny);
            } else {
                String log = inny.getNazwa() + " ZJADŁ MLECZ";
                swiat.dodajDoDziennika(log);
                swiat.usunOrganizm(this);
            }
        }
    }

    @Override
    public String getNazwa() {
        return "Mlecz";
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
