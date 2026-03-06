import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class MainFrame extends JFrame {
    private static final boolean USE_HEX = true;

    private final SwiatInterface swiat;
    private final BoardPanel board;
    private final JTextArea logArea;

    private static final int MAX_WILK          = 3;
    private static final int MAX_OWCA          = 4;
    private static final int MAX_ZOLW          = 2;
    private static final int MAX_LIS           = 1;
    private static final int MAX_ANTYLOPA      = 5;
    private static final int MAX_CYBEROWCA     = 2;
    private static final int MAX_TRAWA         = 3;
    private static final int MAX_MLECZ         = 1;
    private static final int MAX_GUARANA       = 3;
    private static final int MAX_WILCZE_JAGODY = 1;
    private static final int MAX_BARSZCZ       = 4;
    private static final int MAX_HUMAN         = 1;

    public MainFrame() {
        super("Symulator Świata");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        if (USE_HEX) {
            swiat = new SwiatHex();
        } else {
            swiat = new SwiatKwadratowy();
        }
        // 2. Sprawdzamy, czy mamy za dużo maksymalnych organizmów
        int capacity = swiat.getPlanszaX() * swiat.getPlanszaY();
        int maxTotal =
                MAX_WILK
                        + MAX_OWCA
                        + MAX_ZOLW
                        + MAX_LIS
                        + MAX_ANTYLOPA
                        + MAX_CYBEROWCA
                        + MAX_TRAWA
                        + MAX_MLECZ
                        + MAX_GUARANA
                        + MAX_WILCZE_JAGODY
                        + MAX_BARSZCZ
                        + MAX_HUMAN;
        if (maxTotal > capacity) {
            JOptionPane.showMessageDialog(
                    this,
                    "Za mała plansza (" + swiat.getPlanszaX() + "×" + swiat.getPlanszaY() + ")" +
                            " dla " + maxTotal + " organizmów!",
                    "Błąd konfiguracji",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
        }

        initPopulation();



        board = new BoardPanel(swiat);
        board.setPreferredSize(new Dimension(
                /*swiat.getPlanszaX() * 30,
                swiat.getPlanszaY() * 30*/
                1000,1000

        ));
        add(board, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnNext = new JButton("Następna tura");
        JButton btnSave = new JButton("Zapisz grę");
        JButton btnLoad = new JButton("Wczytaj grę");

        btnNext.addActionListener(e -> {
            swiat.wykonajTure(swiat.getLastInput());
            board.repaint();
            appendLogs();
        });

        btnSave.addActionListener(e -> {
            String filename = USE_HEX ? "save_hex.txt" : "save_kwadrat.txt";
            boolean ok = swiat.zapiszDoPliku(filename);
            JOptionPane.showMessageDialog(this, ok ? "Gra została zapisana do " + filename : "Błąd zapisu gry.");
            board.requestFocusInWindow();
        });

        btnLoad.addActionListener(e -> {
            String filename = USE_HEX ? "save_hex.txt" : "save_kwadrat.txt";
            boolean ok = swiat.wczytajZPliku(filename);
            board.repaint();
            appendLogs();
            JOptionPane.showMessageDialog(this, ok ? "Gra została wczytana z " + filename : "Błąd wczytywania gry.");
            board.requestFocusInWindow();
        });


        buttonPanel.add(btnNext);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnLoad);

        add(buttonPanel, BorderLayout.NORTH);


        logArea = new JTextArea(5, swiat.getPlanszaX() * 2);
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);


        KeyAdapter keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char in;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:  in = 'a'; break;
                    case KeyEvent.VK_DOWN: in = 'd'; break;
                    case KeyEvent.VK_LEFT:    in = 'w'; break;
                    case KeyEvent.VK_RIGHT:  in = 's'; break;
                    case KeyEvent.VK_U:     in = 'u'; break;
                    case KeyEvent.VK_E:     in = 'e'; break; // NE
                    case KeyEvent.VK_Q:     in = 'q'; break; // umiejętność
                    case KeyEvent.VK_C:     in = 'c'; break; // NE
                    case KeyEvent.VK_Z:     in = 'z'; break; // NE


                    default: return;
                }
                swiat.wykonajTure(in);
                board.repaint();
                appendLogs();
            }
        };
        addKeyListener(keyListener);
        setFocusable(true);


        pack();
        setLocationRelativeTo(null);

        appendLogs();

        setVisible(true);
        board.repaint();
    }

    private void initPopulation() {
        for (int i = 0; i < MAX_WILK; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new Wilk(9, 5, x, y, swiat, "W"));
        }
        for (int i = 0; i < MAX_OWCA; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new Owca(4, 4, x, y, swiat, "O"));
        }
        for (int i = 0; i < MAX_ZOLW; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new Żółw(2, 1, x, y, swiat, "Z"));
        }
        for (int i = 0; i < MAX_LIS; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new Lis(3, 7, x, y, swiat, "L"));
        }
        for (int i = 0; i < MAX_ANTYLOPA; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new Antylopa(4, 4, x, y, swiat, "A"));
        }
        for (int i = 0; i < MAX_CYBEROWCA; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new Cyberowca(10, 4, x, y, swiat, "C"));
        }
        for (int i = 0; i < MAX_TRAWA; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new Trawa(0, 0, x, y, swiat, "T"));
        }
        for (int i = 0; i < MAX_MLECZ; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new Mlecz(0, 0, x, y, swiat, "M"));
        }
        for (int i = 0; i < MAX_GUARANA; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new Guarana(0, 0, x, y, swiat, "G"));
        }
        for (int i = 0; i < MAX_WILCZE_JAGODY; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new WilczeJagody(99, 0, x, y, swiat, "J"));
        }
        for (int i = 0; i < MAX_BARSZCZ; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new BarszczSosnowskiego(10, 0, x, y, swiat, "B"));
        }
        for (int i = 0; i < MAX_HUMAN; i++) {
            int x = swiat.getFreeX(), y = swiat.getFreeY(x);
            swiat.dodajOrganizm(new Czlowiek(5, 4, x, y, swiat, "H"));
        }
    }

    void appendLogs() {
        List<String> logi = swiat.pobierzLogi();
        for (String linia : logi) {
            logArea.append(linia + "\n");
        }
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}

