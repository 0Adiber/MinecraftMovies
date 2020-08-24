package at.adiber.render;

import at.adiber.io.IOAccess;
import at.adiber.main.Main;
import at.adiber.player.VideoFrame;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class RenderManager {

    private int threads;
    private Location location;
    private BlockFace blockFace;
    private String name;

    private ExecutorService pool;

    public RenderManager(int threads, Location location, BlockFace blockFace, String name) {
        this.threads = threads;
        this.location = location;
        this.blockFace = blockFace;
        this.name = name;
    }

    public RenderManager(Location location, BlockFace blockFace, String name) {
        this(12, location, blockFace, name);
    }

    public void renderFromImages(String folder) throws InterruptedException {

        List<VideoFrame> frames = new ArrayList<>();

        pool = Executors.newFixedThreadPool(threads);
        CompletionService<VideoFrame> service = new ExecutorCompletionService<>(pool);

        List<BufferedImage> images = IOAccess.readImages(folder);

        int position = 0;
        for(BufferedImage img : images) {
            service.submit(new RenderWorker(img, blockFace, location, position++));
        }

        pool.shutdown();

        while(!pool.isTerminated()) {
            Future<VideoFrame> f = service.take();
            try {
                frames.add(f.get());

                System.out.println("Rendered: " + f.get().getPosition());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        frames.sort(VideoFrame::compareTo);
        Main.main.videos.put(name, new Video(frames));
        System.out.println("Render complete");
    }

    public void terminate() throws InterruptedException {
        if(pool != null){
            pool.shutdown(); //no more new tasks
            if(!pool.awaitTermination(30, TimeUnit.SECONDS)) { //wait 30 secs for current tasks to finish
                pool.shutdownNow(); //terminate tasks, if not finished in time
                if (!pool.awaitTermination(30, TimeUnit.SECONDS)) { //wait 30 secs for terminated
                    System.out.println("Renderer Pool did not terminate");
                }
            }
        }
    }

}
