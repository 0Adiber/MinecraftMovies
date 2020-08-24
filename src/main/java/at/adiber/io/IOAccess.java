package at.adiber.io;

import at.adiber.render.RenderWorker;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorService;

public class IOAccess {

    private static boolean stop;

    public static void readImages(String folder, CompletionService service, BlockFace blockFace, Location location) throws IOException{

        int position = 1;
        while(true) {

            if(stop) {
                break;
            }

            if(Runtime.getRuntime().freeMemory() < 100_000_000) {//100 MB => 100 * 1_000_000
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                BufferedImage img = ImageIO.read(new File(folder, (position++) + ".png"));
                service.submit(new RenderWorker(img, blockFace, location, position-1));
            } catch (IOException e) {
                throw new IOException("Read complete");
            }
        }

    }

    public static void kill() {
        stop = true;
    }

}
