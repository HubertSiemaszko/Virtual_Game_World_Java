
import java.util.Random;
import java.awt.Point;


public abstract class Roślina extends Organizm {
    public Roślina(int sila, int inicjatywa, int x, int y,SwiatInterface swiat) {
        super(sila, inicjatywa, x, y, swiat);
    }

    @Override

    public void akcja() {
        if (new Random().nextInt(3) == 1) {
            boolean hex = swiat instanceof SwiatHex;
            Point[] empty = new Point[8];
            int count = 0;

            if (hex) {
                int[][] evenDirs = {{+1,0},{0,-1},{-1,0},{-1,+1},{0,+1},{+1,+1}};
                int[][] oddDirs  = {{+1,-1},{0,-1},{-1,-1},{-1,0},{0,+1},{+1,0}};
                int[][] dirs = (getY() % 2 == 0) ? evenDirs : oddDirs;

                for (int[] dir : dirs) {
                    int nx = getX() + dir[0];
                    int ny = getY() + dir[1];
                    if (nx >= 0 && nx < swiat.getPlanszaX()
                            && ny >= 0 && ny < swiat.getPlanszaY()
                            && swiat.getOrganizmNaPolu(nx, ny) == null) {
                        empty[count++] = new Point(nx, ny);
                    }
                }
            } else {
                int[] DX = {-1,-1,-1,0,0,1,1,1};
                int[] DY = {-1,0,1,-1,1,-1,0,1};
                for (int k = 0; k < 8; k++) {
                    int nx = getX() + DX[k];
                    int ny = getY() + DY[k];
                    if (nx >= 0 && nx < swiat.getPlanszaX()
                            && ny >= 0 && ny < swiat.getPlanszaY()
                            && swiat.getOrganizmNaPolu(nx, ny) == null) {
                        empty[count++] = new Point(nx, ny);
                    }
                }
            }

            if (count > 0) {
                Point choice = empty[new Random().nextInt(count)];
                Roślina dziecko = stworzDziecko(choice.x, choice.y);
                dziecko.ustawWykonanoAkcje(true);
                swiat.dodajOrganizm(dziecko);
                swiat.dodajDoDziennika(
                        getNazwa() + " rozmnożyła się na (" + choice.x + "," + choice.y + ")"
                );
            }
        }
    }


    @Override
    public abstract char rysowanie();


    public abstract Roślina stworzDziecko(int x, int y);


    protected void raport(String tekst) {
        swiat.dodajDoDziennika(tekst);
    }
}