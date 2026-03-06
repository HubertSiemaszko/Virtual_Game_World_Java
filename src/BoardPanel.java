import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;


public class BoardPanel extends JPanel {
    private final SwiatInterface swiat;
    private final int cellSize = 40;
    private final boolean hexMode;
    private final int offsetY = 20;
    private final int offsetX = 20;


    public BoardPanel(SwiatInterface swiat) {
        this.swiat = swiat;
        this.hexMode = swiat instanceof SwiatHex;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col, row;
                if (hexMode) {
                    row = e.getY() / (int)(Math.sqrt(3) * cellSize / 2);
                    col = (e.getX() - (row % 2) * (cellSize/2)) / cellSize;
                } else {
                    col = e.getX() / cellSize;
                    row = e.getY() / cellSize;
                }
                if (col < 0 || row < 0 || col >= swiat.getPlanszaX() || row >= swiat.getPlanszaY()) {
                    return;
                }
                if (swiat.getOrganizmNaPolu(col, row) == null) {
                    String[] opcje = {"Wilk","Owca","Lis","Zolw","Antylopa","Cyberowca",
                            "Trawa","Mlecz","Guarana","WilczeJagody","BarszczSosnowskiego"};
                    String wybor = (String) JOptionPane.showInputDialog(
                            BoardPanel.this, "Wybierz organizm do dodania:",
                            "Dodaj organizm", JOptionPane.PLAIN_MESSAGE,
                            null, opcje, opcje[0]
                    );
                    if (wybor != null) {
                        Organizm nowy = null;
                        switch (wybor) {
                            case "Wilk": nowy = new Wilk(9,5,col,row,swiat,"W"); break;
                            case "Owca": nowy = new Owca(4,4,col,row,swiat,"O"); break;
                            case "Lis": nowy = new Lis(3,7,col,row,swiat,"L"); break;
                            case "Zolw": nowy = new Żółw(2,1,col,row,swiat,"Z"); break;
                            case "Antylopa": nowy = new Antylopa(4,4,col,row,swiat,"A"); break;
                            case "Cyberowca": nowy = new Cyberowca(10,4,col,row,swiat,"C"); break;
                            case "Trawa": nowy = new Trawa(0,0,col,row,swiat,"T"); break;
                            case "Mlecz": nowy = new Mlecz(0,0,col,row,swiat,"M"); break;
                            case "Guarana": nowy = new Guarana(0,0,col,row,swiat,"G"); break;
                            case "WilczeJagody": nowy = new WilczeJagody(99,0,col,row,swiat,"J"); break;
                            case "BarszczSosnowskiego": nowy = new BarszczSosnowskiego(10,0,col,row,swiat,"B"); break;
                        }
                        if (nowy != null) {
                            swiat.dodajOrganizm(nowy);
                            repaint();
                        }
                    }
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(getKeyListenerForCzlowiek());
    }

    @Override
    protected void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cols = swiat.getPlanszaX();
        int rows = swiat.getPlanszaY();

        if (hexMode) {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    Polygon hex = makeHexagon(col, row, cellSize, offsetX, offsetY);
                    g.drawPolygon(hex);
                    Organizm o = swiat.getOrganizmNaPolu(col, row);
                    if (o != null) {
                        Rectangle b = hex.getBounds();
                        g.drawString(String.valueOf(o.rysowanie()),
                                b.x + b.width/2 - 4,
                                b.y + b.height/2 + 4);
                    }
                }
            }
        } else {
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    int x = col * cellSize;
                    int y = row * cellSize;
                    g.drawRect(x, y, cellSize, cellSize);
                    Organizm o = swiat.getOrganizmNaPolu(col, row);
                    if (o != null) {
                        g.drawString(String.valueOf(o.rysowanie()),
                                x + cellSize/2 - 4,
                                y + cellSize/2 + 4);
                    }
                }
            }
        }
    }

    private Polygon makeHexagon(int col, int row, int size, int offsetX, int offsetY) {
        double s = size / 2.0;
        double h = Math.sqrt(3) * s;
        double cx = col * size + (row & 1) * s+offsetX;
        double cy = h * row + offsetY;
        int[] xs = new int[6];
        int[] ys = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i + 30);
            xs[i] = (int) Math.round(cx + s * Math.cos(angle));
            ys[i] = (int) Math.round(cy + s * Math.sin(angle));
        }
        return new Polygon(xs, ys, 6);
    }


    private KeyListener getKeyListenerForCzlowiek() {
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char in = 0;
                boolean hex = swiat instanceof SwiatHex;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:  in = 'a'; break;
                    case KeyEvent.VK_RIGHT: in = 'd'; break;
                    case KeyEvent.VK_UP:    in = 'w'; break;
                    case KeyEvent.VK_DOWN:  in = 's'; break;
                    case KeyEvent.VK_Q:     in = 'q'; break;
                    case KeyEvent.VK_E:     in = 'e'; break;
                    case KeyEvent.VK_Z:     in = 'z'; break;
                    case KeyEvent.VK_C:     in = 'c'; break;
                    case KeyEvent.VK_U:     in = 'u'; break;
                    default: return;
                }
                swiat.wykonajTure(in);
                repaint();
                SwingUtilities.invokeLater(() -> {
                    JFrame top = (JFrame) SwingUtilities.getWindowAncestor(BoardPanel.this);
                    if (top instanceof MainFrame) {
                        ((MainFrame) top).appendLogs();
                    }
                });
            }
        };
    }
}