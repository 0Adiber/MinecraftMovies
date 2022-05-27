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
import java.util.UUID;

public class VideoPlayer {

    private String id;
    private int currentFrame;
    private Video video;

    private Canvas canvas;

    int playerTask = -1;

    private boolean pause;

    private boolean audioLoaded;

    public VideoPlayer(Canvas canvas, Video video, String id) {
        this.video = video;
        this.currentFrame = 0;
        this.canvas = canvas;
        this.id = id;
    }

    public void start(UUID uuid) {

        if(playerTask != -1) {
            return;
        }

        currentFrame = 0;
        audioLoaded = false;

        Main.main.bot.setWatching(video.getName());

        Main.main.bot.getGuildAudioPlayer(Main.main.bot.getApi().getGuilds().get(0))
                .loadAndPlay(new File(Main.main.getDataFolder(), "saves" + File.separator + "audio" + File.separator + video.getName() + ".mp3").getAbsolutePath(), Main.main.bot.getUser(uuid).toString(), this);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!audioLoaded) return;
                if(currentFrame == 0)
                    playerTask = this.getTaskId();
                if(pause)
                    return;
                try {
                    VideoFrame frame = next();
                    canvas.update(frame.getSections());
                }catch(IndexOutOfBoundsException e) {
                    clearShown();
                    stop();
                }

            }
        }.runTaskTimerAsynchronously(Main.main, 0L, 1L);

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
        Main.main.bot.getGuildAudioPlayer(Main.main.bot.getApi().getGuilds().get(0)).disconnect();
    }

    public void pause() {
        this.pause = true;
    }

    public void resume() {
        this.pause = false;
    }

    public VideoFrame next() {
        return this.video.getFrames().get(currentFrame++);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setAudioLoaded(boolean audioLoaded) {
        this.audioLoaded = audioLoaded;
    }
}
