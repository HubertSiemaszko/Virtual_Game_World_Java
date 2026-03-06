
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Czlowiek extends Zwierze {
    private int cooldownLeft;
    private int activeTurnsLeft;
    private final String znak;

    public Czlowiek(int sila, int inicjatywa, int x, int y, SwiatInterface swiat, String znak) {
        super(sila, inicjatywa, x, y, swiat);
        this.cooldownLeft = 0;
        this.activeTurnsLeft = 0;
        this.znak = znak;
    }

    @Override
    public void akcja() {
        zapiszPozycje();
        char input = swiat.getLastInput();

        if (input == 'u') {
            aktywujUmiejetnosc();
        }
        int newX = getX();
        int newY = getY();
        boolean hex = swiat instanceof SwiatHex;

        switch (input) {
            case 'a': if (!hex&&newY > 0) newY--; break;
            case 'd': if (!hex&&newY < swiat.getPlanszaY() - 1) newY++; break;
            case 'w': if (newX > 0) newX--; break;
            case 's': if (newX < swiat.getPlanszaX() - 1) newX++; break;
            case 'q':
                if (hex) {
                    if (getY() % 2 == 0) {
                        newX--;
                        newY--;
                    } else {
                        newY--;
                    }
                }
                break;
            case 'e':
                if (hex) {
                    if (getY() % 2 != 0) {
                        newX++;
                        newY--;
                    } else {
                        newY--;
                    }
                }
                break;
            case 'c':
                if (hex) {
                    if (getY() % 2 != 0) {
                        newX++;
                        newY++;
                    } else {
                        newY++;
                    }
                }
                break;
            case 'z':
                if (hex) {
                    if (getY() % 2 == 0) {
                        newX--;
                        newY++;
                    } else {
                        newY++;
                    }
                }
                break;
            default: break;
        }
        newX = Math.max(0, Math.min(newX, swiat.getPlanszaX() - 1));
        newY = Math.max(0, Math.min(newY, swiat.getPlanszaY() - 1));
        setPozycja(newX, newY);
        swiat.dodajDoDziennika(
                "Czlowiek przesunal sie na (" + newX + "," + newY + ")"
        );

        if (activeTurnsLeft > 0) {
            swiat.dodajDoDziennika(
                    "Tarcza Alzura aktywna, pozostalo tur: " + activeTurnsLeft
            );
            activeTurnsLeft--;
            if (activeTurnsLeft == 0) {
                cooldownLeft = 5;
                swiat.dodajDoDziennika(
                        "Tarcza Alzura wygasla, cooldown: " + cooldownLeft
                );
            }
        } else if (cooldownLeft > 0) {
            cooldownLeft--;
            swiat.dodajDoDziennika(
                    "Cooldown Tarczy Alzura, pozostalo tur: " + cooldownLeft
            );
        }
    }

    @Override
    public void kolizja(Organizm inny) {
        if (inny == null) return;
        if (inny.getX() == getX() && inny.getY() == getY()) {
            if (tarczaAlzura(inny)) return;
            if (getSila() >= inny.getSila()) {
                swiat.dodajDoDziennika("Czlowiek zabil " + inny.getNazwa());
                swiat.usunOrganizm(inny);
            } else {
                swiat.dodajDoDziennika(inny.getNazwa() + " zabil Czlowieka");
                swiat.usunOrganizm(this);
            }
        }
    }

    @Override
    public String getNazwa() {
        return "Czlowiek";
    }

    @Override
    public char rysowanie() {
        return znak.charAt(0);
    }

    @Override
    public Zwierze stworzDziecko(int x, int y) {
        return new Czlowiek(5, 5, x, y, swiat, znak);
    }

    @Override
    public boolean czyOdbilAtak(Organizm atakujacy) {
        return false;
    }

    public boolean tarczaAlzura(Organizm atakujacy) {
        if (activeTurnsLeft > 0) {
            List<Point> free = new ArrayList<>();
            int[] DX = {-1,-1,-1,0,0,1,1,1};
            int[] DY = {-1,0,1,-1,1,-1,0,1};
            for (int k = 0; k < DX.length; k++) {
                int nx = getX() + DX[k], ny = getY() + DY[k];
                if (nx >= 0 && nx < swiat.getPlanszaX() && ny >= 0 && ny < swiat.getPlanszaY()
                        && swiat.getOrganizmNaPolu(nx, ny) == null) {
                    free.add(new Point(nx, ny));
                }
            }
            if (!free.isEmpty()) {
                Point p = free.get(new Random().nextInt(free.size()));
                atakujacy.setPozycja(p.x, p.y);
                swiat.dodajDoDziennika(
                        "Tarcza Alzura odparla " + atakujacy.getNazwa() +
                                " na (" + p.x + "," + p.y + ")"
                );
                return true;
            }
        }
        return false;
    }

    public void aktywujUmiejetnosc() {
        if (activeTurnsLeft == 0 && cooldownLeft == 0) {
            activeTurnsLeft = 5;
            swiat.dodajDoDziennika(
                    "Czlowiek aktywowal Tarcze Alzura! Pozostalo tur: " + activeTurnsLeft
            );
        }
    }

    public int getActiveTurns() { return activeTurnsLeft; }
    public int getCooldown() { return cooldownLeft; }
    public void setActiveTurns(int t) { activeTurnsLeft = t; }
    public void setCooldown(int t) { cooldownLeft = t; }

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
