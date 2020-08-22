package at.adiber.player;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;

public class VideoPlayer {

    private FFmpegFrameGrabber grabber;
    private Canvas canvas;

    public VideoPlayer(String file, Canvas canvas) {
        grabber = new FFmpegFrameGrabber(file);
        this.canvas = canvas;
    }

    public void start() throws FrameGrabber.Exception {
        grabber.start();
    }

    public void stop() throws FrameGrabber.Exception {
        grabber.stop();
    }

    public BufferedImage next() throws FrameGrabber.Exception {
        Java2DFrameConverter converter = new Java2DFrameConverter();
        return converter.convert(grabber.grab());
    }

}
