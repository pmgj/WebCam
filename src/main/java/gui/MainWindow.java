package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class MainWindow extends JFrame {

    private JMenuBar menuBar;
    private JPanel cards;
    private final StatusBar statusBar = new StatusBar();

    private final Camera cam = new Camera();

    private void setVideoPanel() {
        JPanel panel = cam.openCamera();
        this.statusBar.setMessage(panel == null ? "Nenhuma câmera foi detectada!" : "Câmera detectada!");

        if (cards != null) {
            this.getContentPane().remove(0);
        }
        cards = (panel != null) ? panel : new JPanel();
        this.getContentPane().add(cards, 0);
        this.setVisible(true);
    }

    private void closeVideo() {
        this.statusBar.setMessage("Webcam desabilitada.");
        cam.closeWebcam();
        if (cards != null) {
            this.getContentPane().remove(0);
        }
        cards = new JPanel();
        this.getContentPane().add(cards, 0);
        this.setVisible(true);
    }

    private void saveVideo() {
        this.statusBar.setMessage("Iniciando gravação de vídeo...");
        cam.saveVideo(this.statusBar);
    }

    private void setTelaCheia() {
        if (this.getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setUndecorated(true);
            this.menuBar.setVisible(false);
        } else {
            this.setExtendedState(JFrame.NORMAL);
            this.setUndecorated(false);
            this.menuBar.setVisible(true);
        }
    }

    private void createMenu() {
        this.menuBar = new JMenuBar();
        JMenu menu = new JMenu("Arquivo");
        menu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(menu);

        JMenuItem menuItemIniciar = new JMenuItem("Iniciar Câmera", KeyEvent.VK_I);
        menuItemIniciar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItemIniciar.addActionListener((evt) -> setVideoPanel());
        menu.add(menuItemIniciar);

        JMenuItem menuItemFecharCamera = new JMenuItem("Fechar Câmera", KeyEvent.VK_F);
        menuItemFecharCamera.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItemFecharCamera.addActionListener(evt -> closeVideo());
        menu.add(menuItemFecharCamera);

        JMenuItem menuItemSalvar = new JMenuItem("Salvar", KeyEvent.VK_S);
        menuItemSalvar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
        menuItemSalvar.addActionListener(evt -> saveVideo());
        menu.add(menuItemSalvar);

        JMenuItem menuItemPrefs = new JMenuItem("Preferências", KeyEvent.VK_P);
        menuItemPrefs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
        menuItemPrefs.addActionListener(evt -> new PreferencesWindow());
        menu.add(menuItemPrefs);

        JMenuItem menuItemTelaCheia = new JMenuItem("Tela Cheia", KeyEvent.VK_T);
        menuItemTelaCheia.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
        menuItemTelaCheia.addActionListener(evt -> setTelaCheia());
        menu.add(menuItemTelaCheia);

        JMenuItem menuItemFechar = new JMenuItem("Fechar", KeyEvent.VK_E);
        menuItemFechar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
        menuItemFechar.addActionListener(evt ->System.exit(0));
        menu.add(menuItemFechar);
        this.setJMenuBar(menuBar);
    }

    public MainWindow(String title) {
        super(title);
        this.setPreferredSize(new Dimension(300, 300));
        this.setLayout(new BorderLayout());
        this.createMenu();
        this.setVideoPanel();
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        new MainWindow("Webcam Capture");
    }
}
