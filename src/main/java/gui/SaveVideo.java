package gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.video.ConverterFactory;

public class SaveVideo implements Runnable {

    private IMediaWriter writer;
    private final Webcam webcam;
    private final StatusBar statusBar;

    public SaveVideo(Webcam webcam, StatusBar statusBar) {
        this.webcam = webcam;
        this.statusBar = statusBar;
    }

    public IAudioSamples customAudioStream(long length) {
        int channelCount = 2;
        int sampleRate = 44100;

        // IContainer container = writer.getContainer();
        // IStream stream = container.getStream(1);

        IAudioSamples samples = IAudioSamples.make(1024 * 5, channelCount, IAudioSamples.Format.FMT_S16);

        TargetDataLine line = null;
        AudioFormat audioFormat = new AudioFormat(sampleRate, (int) 16, channelCount, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();
        } catch (LineUnavailableException ex) {
            System.out.println("ERROR: " + ex.toString());
        }

        byte[] data = new byte[4096 * 5];
        int sz = line.read(data, 0, data.length);

        samples.put(data, 0, 0, sz);
        int audioTime = 0;
        audioTime += sz;

        samples.setComplete(true, sz / 4, sampleRate, channelCount, IAudioSamples.Format.FMT_S16, audioTime / 4);

        line.stop();
        line.close();
        return (samples);
    }

    @Override
    public void run() {
        long nanoSecond = 1000000000;
        int sampleRate = 44100, channels = 2;

        Preferences prefs = Preferences.userNodeForPackage(PreferencesWindow.class);
        String dir = prefs.get(PreferencesWindow.OUTPUT_DIR, ".");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        LocalDateTime currentTime = LocalDateTime.now();
        var d = dtf.format(currentTime);

        writer = ToolFactory.makeWriter(dir + "\\" + d + ".mp4");

        Dimension size = WebcamResolution.QVGA.getSize();
        writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_H264, size.width, size.height);
        writer.addAudioStream(1, 0, ICodec.ID.CODEC_ID_MP3, channels, sampleRate);
        long startTime = System.nanoTime();
        int time = prefs.getInt(PreferencesWindow.STORE_TIME, 10);
        while (true) {
            BufferedImage image = ConverterFactory.convertToType(this.webcam.getImage(), BufferedImage.TYPE_3BYTE_BGR);
            long now = System.nanoTime();
            writer.encodeVideo(0, image, now - startTime, TimeUnit.NANOSECONDS);
            IAudioSamples samples = this.customAudioStream(now - startTime);
            writer.encodeAudio(1, samples);
            long elapsedTime = now - startTime;
            if (elapsedTime >= nanoSecond * (time + 1)) {
                break;
            } else {
                this.statusBar.setMessage("Gravando vídeo... (" + elapsedTime / nanoSecond + " s)");
            }
        }
        writer.close();
        this.statusBar.setMessage("Gravação de vídeo finalizada.");
    }
}
