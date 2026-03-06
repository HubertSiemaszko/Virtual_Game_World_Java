import java.awt.*;
import java.util.List;


public class WilczeJagody extends Roślina {
    private final String znak;

    public WilczeJagody(int sila, int inicjatywa, int x, int y, SwiatInterface swiat, String znak) {
        super(sila, inicjatywa, x, y, swiat);
        this.znak = znak;
    }

    @Override
    public void akcja() {
        if (czyWykonanoAkcje()) return;
        super.akcja();
        ustawWykonanoAkcje(true);
    }

    @Override
    public Roślina stworzDziecko(int x, int y) {
        return new WilczeJagody(99, 0, x, y, swiat, "J");
    }

    @Override
    public String getNazwa() {
        return "WilczeJagody";
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
        if (inny.getX() == getX() && inny.getY() == getY()) {
            if (getSila() > inny.getSila()) {
                String log = "WILCZE JAGODY zabiły " + inny.getNazwa();
                swiat.dodajDoDziennika(log);
                swiat.usunOrganizm(inny);
            } else {
                String log = inny.getNazwa() + " zjadł Wilcze Jagody i zmarł";
                swiat.dodajDoDziennika(log);
                swiat.usunOrganizm(this);
                swiat.usunOrganizm(inny);
            }
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
