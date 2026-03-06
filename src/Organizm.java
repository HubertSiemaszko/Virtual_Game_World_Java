public abstract class Organizm implements SwiatInterface{
    protected int sila;
    protected int inicjatywa;
    protected int x, y;
    protected int wiek;
    protected boolean zyje;
    protected SwiatInterface swiat;
    private int prevX, prevY;
    private boolean wykonanoAkcje;

    public Organizm(int sila, int inicjatywa,  int x, int y, SwiatInterface swiat) {
        this.sila = sila;
        this.inicjatywa = inicjatywa;
        this.x = x;
        this.y = y;
        this.wiek = 0;
        this.zyje = true;
        this.swiat = swiat;
        this.prevX = x;
        this.prevY = y;
        this.wykonanoAkcje = false;
    }

    public int getSila()               { return sila; }
    public int getInicjatywa()         { return inicjatywa; }
    public int getX()                  { return x; }
    public int getY()                  { return y; }
    public int getWiek()               { return wiek; }
    public boolean czyZyje()           { return zyje; }
    public SwiatInterface getSwiat()            { return swiat; }
    public void setSwiat(SwiatInterface swiat)  { this.swiat = swiat; }
    public boolean czyWykonanoAkcje()  { return wykonanoAkcje; }
    public void ustawWykonanoAkcje(boolean stan) { this.wykonanoAkcje = stan; }

    public void setPozycja(int noweX, int noweY) {
        this.x = noweX;
        this.y = noweY;
    }
    public void zapiszPozycje() {
        this.prevX = this.x;
        this.prevY = this.y;
    }
    public void przywrocPoprzedniaPozycje() {
        this.x = this.prevX;
        this.y = this.prevY;
    }

    public void setZyje(boolean stan)  { this.zyje = stan; }
    public void zwiekszWiek()           { this.wiek++; }
    public void setWiek(int nowyWiek)  { this.wiek = nowyWiek; }

    public void setSila(int nowaSila)  { this.sila = nowaSila; }


    public abstract String getNazwa();


    public abstract void akcja();


    public abstract void kolizja(Organizm intruz);

    public abstract char rysowanie();
}
