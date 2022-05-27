package at.adiber.render;

import at.adiber.player.VideoFrame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class RenderProducer implements Runnable{

    private volatile boolean runFlag;

    private CompletionService<VideoFrame> service;
    private Path folder;
    private ExecutorService pool;

    public RenderProducer(CompletionService<VideoFrame> service, Path folder, ExecutorService pool) {
        this.service = service;
        this.folder = folder;
        this.pool = pool;
    }

    @Override
    public void run() {

        AtomicInteger position = new AtomicInteger(1);
        runFlag = true;

        while(runFlag) {
            if (Runtime.getRuntime().freeMemory() < 250 * 1_000_000) {//250 MB
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                int pos = position.getAndIncrement();
                BufferedImage img = ImageIO.read(new File(folder.toString(), pos + ".png"));
                service.submit(new RenderWorker(img, pos));
            } catch (IOException e) {
                Bukkit.getLogger().info("Read complete");
                stop();
            } catch (OutOfMemoryError e) {
                Bukkit.getLogger().severe("Out of memory reading Images - Sleeping for 1 second");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                continue;
            }
        }

    }

    public void stop() {
        runFlag = false;
        pool.shutdown();
    }

    public void terminate() {
        runFlag = false;
        pool.shutdownNow();
    }
}
