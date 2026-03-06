import java.awt.*;
import java.util.List;
import java.util.Random;


public class Lis extends Zwierze {
    private final String znak;

    public Lis(int sila, int inicjatywa, int x, int y, SwiatInterface swiat, String znak) {
        super(sila, inicjatywa, x, y, swiat);
        this.znak = znak;
    }

    @Override
    public void akcja() {
        int oldX = getX();
        int oldY = getY();
        boolean hex = swiat instanceof SwiatHex;
        Random rnd = new Random();
        int newX = oldX;
        int newY = oldY;

        if (hex) {
            int[][] evenDirs = {{+1,0},{0,-1},{-1,0},{-1,+1},{0,+1},{+1,+1}};
            int[][] oddDirs  = {{+1,-1},{0,-1},{-1,-1},{-1,0},{0,+1},{+1,0}};
            int[][] dirs = (oldY % 2 == 0) ? evenDirs : oddDirs;
            int i = rnd.nextInt(dirs.length);
            newX = oldX + dirs[i][0];
            newY = oldY + dirs[i][1];
        } else {
            int[] DX = {-1,  0,  1, -1, 1, -1,  0,  1};
            int[] DY = {-1, -1, -1,  0, 0,  1,  1,  1};
            int i = rnd.nextInt(DX.length);
            newX = oldX + DX[i];
            newY = oldY + DY[i];
        }

        if (newX < 0 || newX >= swiat.getPlanszaX() || newY < 0 || newY >= swiat.getPlanszaY()) {
            return;
        }

        Organizm target = swiat.getOrganizmNaPolu(newX, newY);
        if (target == null || target.getSila() <= this.getSila()) {
            setPozycja(newX, newY);
            swiat.dodajDoDziennika(
                    "Lis przesunal sie z miejsca (" + oldX + "," + oldY + ") na miejsce (" + newX + "," + newY + ")"
            );
            if (target != null) {
                swiat.usunOrganizm(target);
                swiat.dodajDoDziennika(
                        "Lis zabil " + target.getNazwa() + " na (" + newX + "," + newY + ")"
                );
            }
        }
    }

    @Override
    public boolean czyOdbilAtak(Organizm atakujacy) {
        return false;
    }

    @Override
    public Zwierze stworzDziecko(int x, int y) {
        return new Lis(3, 7, x, y, swiat, znak);
    }

    @Override
    public String getNazwa() {
        return "Lis";
    }

    @Override
    public char rysowanie() {
        return znak.charAt(0);
    }

    @Override
    public void kolizja(Organizm inny) {
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
