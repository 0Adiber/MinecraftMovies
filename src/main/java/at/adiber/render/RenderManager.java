package at.adiber.render;

import at.adiber.io.IOAccess;
import at.adiber.main.Main;
import at.adiber.player.VideoFrame;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    /**
     * Renders a folder of images or a mp4
     * @param folder The folder/file
     * @param type true=folder;false=file
     * @throws InterruptedException
     */
    public void renderFromImages(String folder, boolean type) throws InterruptedException {

        List<VideoFrame> frames = new ArrayList<>();

        pool = Executors.newFixedThreadPool(threads);
        CompletionService<VideoFrame> service = new ExecutorCompletionService<>(pool);

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (type) {
                        int position = 1;
                        while(new File(folder, position + ".png").exists()) {
                            service.submit(new RenderWorker(folder, blockFace, location, position++));
                        }
                        pool.shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pool.shutdown();
                }
            }
        }.runTaskAsynchronously(Main.main);

        while(!pool.isTerminated()) {
            Future<VideoFrame> f = service.take();
            try {
                frames.add(f.get());

                System.out.println("Rendered: " + f.get().getPosition());
            } catch (ExecutionException e) {
                System.out.println("Out of memory on Threads");
            }
        }

        frames.sort(VideoFrame::compareTo);
        Main.main.videos.put(name, new Video(frames));
        System.out.println("Render complete");
    }

    public void terminate() throws InterruptedException {
        IOAccess.kill();
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
