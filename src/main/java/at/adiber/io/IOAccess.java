package at.adiber.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IOAccess {

    private static boolean stop;

    public static BufferedImage readImages(String folder, int position) throws IOException {

        while (!stop) {

            if (Runtime.getRuntime().freeMemory() < 100 * 1_000_000) {//100 MB => 100 * 1_000_000
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            try {
                return ImageIO.read(new File(folder, position + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("Read complete");
            } catch (OutOfMemoryError e) {
                System.out.println("Out of memory reading Images");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                continue;
            }
        }

        return null;
    }

/*
    public static void readVideo(String file, CompletionService service, BlockFace blockFace, Location location) throws IOException {
        FFmpegFrameGrabber grabber;
        grabber = new FFmpegFrameGrabber(file);
        Java2DFrameConverter converter = new Java2DFrameConverter();

        grabber.start();
        int position = 1;
        while(true) {

            if(stop){
                break;
            }

            if(Runtime.getRuntime().freeMemory() < 100_000_000) {//100 MB => 100 * 1_000_000
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            try {
                BufferedImage img = converter.convert(grabber.grabImage());
                if(img == null) {
                    grabber.stop();
                    throw new IOException("Read complete");
                }
                service.submit(new RenderWorker(img, blockFace, location, position++));
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
                grabber.stop();
                throw new IOException("Read complete");
            }

        }
    }*/

    public static void kill() {
        stop = true;
    }

}
