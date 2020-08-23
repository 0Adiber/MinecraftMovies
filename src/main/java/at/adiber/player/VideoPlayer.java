package at.adiber.player;

import at.adiber.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoPlayer {

    //private FFmpegFrameGrabber grabber;
    private String folder;
    private List<VideoFrame> frames;
    private int currentFrame;

    private BlockFace direction;
    private Location location;
    private Canvas canvas;

    int playerTask = -1;

    public VideoPlayer(Canvas canvas, String folder, BlockFace direction, Location location) {
        //grabber = new FFmpegFrameGrabber(file);
        this.folder = folder;
        this.frames = new ArrayList<>();
        this.currentFrame = 0;
        this.direction = direction;
        this.location = location;
        this.canvas = canvas;
    }

    public void render() {

        VideoPlayer vp = this;

        new BukkitRunnable() {
            int i = 1;
            @Override
            public void run() {
                while(true) {
                    try {
                        frames.add(new VideoFrame(direction, location, ImageIO.read(new File(folder + File.separator + (i++) + ".png"))));
                        System.out.println("Rendered: " + (i-1));
                    } catch (IOException e) {
                        System.out.println("Render finished");
                        break;
                    }
                }

                Main.main.videos.put("test", vp);

                start();
                this.cancel();

            }
        }.runTaskAsynchronously(Main.main);

    }

    public void start() {

        if(playerTask != -1) {
            return;
        }
        currentFrame = 0;

        new BukkitRunnable() {
            @Override
            public void run() {
                playerTask = this.getTaskId();
                try {
                    VideoFrame frame = next();
                    canvas.update(frame.getSections());
                }catch(IndexOutOfBoundsException e) {
                    clearShown();
                    stop();
                }
            }
        }.runTaskTimer(Main.main, 0L, 1L);
    }

    public void clearShown() {
        canvas.destroy();
        for(VideoFrame frame : frames) {
            for(CanvasSection section : frame.sections) {
                section.clearShown();
            }
        }
    }

    public void stop() {
        if(playerTask != -1) {
            Bukkit.getScheduler().cancelTask(playerTask);
            playerTask = -1;
        }
    }

    public VideoFrame next() {
        //Java2DFrameConverter converter = new Java2DFrameConverter();
        //return converter.convert(grabber.grab());
        return this.frames.get(currentFrame++);
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
