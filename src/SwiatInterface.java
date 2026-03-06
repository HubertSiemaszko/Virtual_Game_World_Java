import java.awt.Point;
import java.io.IOException;
import java.util.List;

public interface SwiatInterface {
    void wykonajTure(char input);

    char getLastInput();

    int getPlanszaX();

    int getPlanszaY();

    int getFreeX();

    int getFreeY(int x);

    void dodajOrganizm(Organizm org);

    List<Organizm> getOrganizmy();

    void usunOrganizm(Organizm org);

    Organizm getOrganizmNaPolu(int x, int y);

    Point znajdzPusteObok(int x, int y);

    void dodajDoDziennika(String tekst);

    List<String> pobierzLogi();

    boolean zapiszDoPliku(String nazwaPliku);

    boolean wczytajZPliku(String nazwaPliku);
}
