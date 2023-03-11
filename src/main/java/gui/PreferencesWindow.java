package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PreferencesWindow {

    Preferences prefs = Preferences.userNodeForPackage(PreferencesWindow.class);

    public static final String STORE_TIME = "Store time in seconds";
    public static final String OUTPUT_DIR = "Directory to save videos";

    private static final Insets WEST_INSETS = new Insets(5, 0, 5, 5);
    private static final Insets EAST_INSETS = new Insets(5, 5, 5, 0);

    private GridBagConstraints createGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;

        gbc.anchor = (x == 0) ? GridBagConstraints.WEST : GridBagConstraints.EAST;
        gbc.fill = (x == 0) ? GridBagConstraints.BOTH
                : GridBagConstraints.HORIZONTAL;

        gbc.insets = (x == 0) ? WEST_INSETS : EAST_INSETS;
        gbc.weightx = (x == 0) ? 0.1 : 1.0;
        gbc.weighty = 1.0;
        return gbc;
    }

    public PreferencesWindow() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc;

        gbc = createGbc(0, 0);
        panel.add(new JLabel("Tempo do video:"), gbc);

        JTextField time = new JTextField(prefs.get(STORE_TIME, "10"), 10);
        time.setSize(new Dimension(200, 10));
        gbc = createGbc(1, 0);
        panel.add(time, gbc);
        
        gbc = createGbc(0, 2);
        panel.add(new JLabel("Diretório padrão:"), gbc);

        gbc = createGbc(1, 2);
        JPanel dirSel = new JPanel();
        dirSel.add(new JLabel(prefs.get(OUTPUT_DIR, ".")));
        JButton procurar = new JButton("Procurar...");
        procurar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser = new JFileChooser();
                File f = new File(prefs.get(OUTPUT_DIR, "."));
                chooser.setCurrentDirectory(f);
                chooser.setDialogTitle("Selecione um diretório...");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File myFile = chooser.getSelectedFile();
                    prefs.put(OUTPUT_DIR, myFile.getAbsolutePath());
                }
            }
        });
        dirSel.add(procurar);
        panel.add(dirSel, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Preferências", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            prefs.put(STORE_TIME, time.getText());
        }
    }

    public static void main(String[] args) {
        new PreferencesWindow();
    }
}
