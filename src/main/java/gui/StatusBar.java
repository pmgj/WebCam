package gui;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class StatusBar extends JLabel {

    public StatusBar() {
        super();
        super.setPreferredSize(new Dimension(100, 16));
        setMessage("Pronto");
        this.setHorizontalAlignment(SwingConstants.RIGHT);
    }

    public void setMessage(String message) {
        setText(" " + message);
    }
}
