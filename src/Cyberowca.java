
import java.awt.Point;
import java.util.List;


public class Cyberowca extends Zwierze {
    private final String znak;

    public Cyberowca(int sila, int inicjatywa, int x, int y, SwiatInterface swiat, String znak) {
        super(sila, inicjatywa, x, y,swiat);
        this.znak = znak;
    }

    @Override
    public boolean czyOdbilAtak(Organizm atakujacy) {
        return false;
    }

    @Override
    public void akcja() {
        zapiszPozycje();
        int minDist = swiat.getPlanszaX() + swiat.getPlanszaY();
        Organizm target = null;
        List<Organizm> lista = swiat.getOrganizmy();
        for (Organizm o : lista) {
            if (o instanceof BarszczSosnowskiego && o.czyZyje()) {
                int dist = Math.abs(o.getX() - getX()) + Math.abs(o.getY() - getY());
                if (dist < minDist) {
                    minDist = dist;
                    target = o;
                }
            }
        }
        if (target != null) {
            int dx = target.getX() - getX();
            int dy = target.getY() - getY();
            if (Math.abs(dx) > Math.abs(dy)) {
                setPozycja(getX() + (dx > 0 ? 1 : -1), getY());
            } else if (dy != 0) {
                setPozycja(getX(), getY() + (dy > 0 ? 1 : -1));
            }
        } else {
            super.akcja();
        }
        swiat.dodajDoDziennika(
                "Cyberowca przesunął się z (" + getPrevX() + "," + getPrevY() + ") na (" + getX() + "," + getY() + ")"
        );
    }

    @Override
    public Zwierze stworzDziecko(int x, int y) {
        return new Cyberowca(10, 4, x, y, swiat, znak);
    }

    @Override
    public String getNazwa() {
        return "Cyberowca";
    }

    @Override
    public char rysowanie() {
        return znak.charAt(0);
    }

    @Override
    public void kolizja(Organizm inny) {
        if (inny == null) return;
        if (inny.getX() != getX() || inny.getY() != getY()) return;

        if (inny instanceof Cyberowca) {
            Point free = swiat.znajdzPusteObok(getX(), getY());
            if (free.x >= 0 && free.y >= 0) {
                Zwierze dziecko = stworzDziecko(free.x, free.y);
                dziecko.ustawWykonanoAkcje(true);
                swiat.dodajOrganizm(dziecko);
                swiat.dodajDoDziennika(
                        "Cyberowca rozmnożył się na (" + free.x + "," + free.y + ")"
                );
                ustawWykonanoAkcje(true);
                inny.ustawWykonanoAkcje(true);
            }
            return;
        }

        if (getSila() > inny.getSila()) {
            swiat.dodajDoDziennika("Cyberowca zjadł " + inny.getNazwa());
            swiat.usunOrganizm(inny);
        } else if (inny instanceof BarszczSosnowskiego) {
            swiat.dodajDoDziennika("Cyberowca zjadł Barszcz Sosnowskiego");
            swiat.usunOrganizm(inny);
        } else {
            swiat.dodajDoDziennika(inny.getNazwa() + " zjadł Cyberowcę");
            swiat.usunOrganizm(this);
        }
    }

    private int getPrevX() {
        try {
            java.lang.reflect.Field f = Organizm.class.getDeclaredField("prevX");
            f.setAccessible(true);
            return f.getInt(this);
        } catch (Exception e) {
            return getX();
        }
    }
    private int getPrevY() {
        try {
            java.lang.reflect.Field f = Organizm.class.getDeclaredField("prevY");
            f.setAccessible(true);
            return f.getInt(this);
        } catch (Exception e) {
            return getY();
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

