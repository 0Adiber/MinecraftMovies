package at.adiber.player;

import at.adiber.main.Main;
import at.adiber.render.Video;
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
    private String id;
    private int currentFrame;
    private Video video;

    private Canvas canvas;

    int playerTask = -1;

    public VideoPlayer(Canvas canvas, Video video, String id) {
        //grabber = new FFmpegFrameGrabber(file);
        this.video = video;
        this.currentFrame = 0;
        this.canvas = canvas;
        this.id = id;
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
        canvas.destroyAll();
        for(VideoFrame frame : video.getFrames()) {
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

        Main.main.players.remove(this.id);
    }

    public VideoFrame next() {
        //Java2DFrameConverter converter = new Java2DFrameConverter();
        //return converter.convert(grabber.grab());
        return this.video.getFrames().get(currentFrame++);
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
