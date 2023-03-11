package gui;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.Dimension;
import javax.swing.JPanel;

public class Camera {

    private Webcam webcam;

    public void closeWebcam() {
        this.webcam.close();
    }

    protected JPanel openCamera() {
        try {
            Dimension size = WebcamResolution.QVGA.getSize();
            webcam = Webcam.getDefault();
            webcam.setViewSize(size);
            webcam.open(true);

            WebcamPanel panel = new WebcamPanel(webcam);
            panel.setMirrored(true);
            return panel;
        } catch (NullPointerException ex) {
            return null;
        }
    }

    public void saveVideo(StatusBar status) {
        SaveVideo sv = new SaveVideo(webcam, status);
        Thread thread = new Thread(sv);
        thread.start();
    }
}
