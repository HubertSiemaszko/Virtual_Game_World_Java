import java.awt.Point;


public abstract class Zwierze extends Organizm {
    public Zwierze(int sila, int inicjatywa, int x, int y, SwiatInterface swiat) {
        super(sila, inicjatywa, x, y, swiat);
    }

    @Override
    public void akcja() {
        zapiszPozycje();
        boolean hex = swiat instanceof SwiatHex;
        java.util.Random rnd = new java.util.Random();

        int newX = x;
        int newY = y;

        if (hex) {
            int[][] evenDirs = {{+1,0},{0,-1},{-1,0},{-1,+1},{0,+1},{+1,+1}};
            int[][] oddDirs  = {{+1,-1},{0,-1},{-1,-1},{-1,0},{0,+1},{+1,0}};
            int[][] dirs = (y % 2 == 0) ? evenDirs : oddDirs;
            int i = rnd.nextInt(dirs.length);
            newX = x + dirs[i][0];
            newY = y + dirs[i][1];
        } else {
            int[] DX = {-1, 0, 1, -1, 1, -1, 0, 1};
            int[] DY = {-1, -1, -1, 0, 0, 1, 1, 1};
            int i = rnd.nextInt(DX.length);
            newX = x + DX[i];
            newY = y + DY[i];
        }

        if (newX >= 0 && newX < swiat.getPlanszaX() && newY >= 0 && newY < swiat.getPlanszaY()) {
            x = newX;
            y = newY;
        }
    }

    @Override
    public void kolizja(Organizm inny) {
        if (inny == null) return;
        if (inny.getX() != x || inny.getY() != y) return;
        if (inny.getClass() == this.getClass()) {
            rozmnoz((Zwierze) inny);
        } else {
            walcz(inny);
        }
    }

    @Override
    public abstract char rysowanie();

    public abstract Zwierze stworzDziecko(int x, int y);

    public abstract boolean czyOdbilAtak(Organizm atakujacy);

    protected void rozmnoz(Zwierze partner) {
        if (czyWykonanoAkcje() && partner.czyWykonanoAkcje()) return;
        Point puste = swiat.znajdzPusteObok(x, y);
        if (puste.x >= 0) {
            Zwierze dziecko = stworzDziecko(puste.x, puste.y);
            dziecko.ustawWykonanoAkcje(true);
            swiat.dodajOrganizm(dziecko);
            swiat.dodajDoDziennika(
                    getNazwa() + " i " + partner.getNazwa() +
                            " rozmnozyli sie na (" + puste.x + "," + puste.y + ")"
            );
            ustawWykonanoAkcje(true);
            partner.ustawWykonanoAkcje(true);
        }
    }

    protected void walcz(Organizm przeciwnik) {
        if (czyOdbilAtak(przeciwnik)) return;
        if (sila >= przeciwnik.getSila()) {
            swiat.dodajDoDziennika(
                    getNazwa() + " zabil " + przeciwnik.getNazwa() +
                            " na (" + x + "," + y + ")"
            );
            swiat.usunOrganizm(przeciwnik);
        } else {
            swiat.dodajDoDziennika(
                    przeciwnik.getNazwa() + " zabil " + getNazwa() +
                            " na (" + x + "," + y + ")"
            );
            swiat.usunOrganizm(this);
        }
    }

    protected void raport(String tekst) {
        swiat.dodajDoDziennika(tekst);
    }
}
