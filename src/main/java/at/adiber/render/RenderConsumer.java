package at.adiber.render;

import at.adiber.main.Main;
import at.adiber.player.VideoFrame;
import at.adiber.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class RenderConsumer implements Runnable{

    private CompletionService<VideoFrame> service;
    private CommandSender sender;
    private String name;
    private ExecutorService pool;

    public RenderConsumer(CompletionService<VideoFrame> service, CommandSender sender, String name, ExecutorService pool) {
        this.service = service;
        this.sender = sender;
        this.name = name;
        this.pool = pool;
    }

    @Override
    public void run() {
        List<VideoFrame> frames = new ArrayList<>();

        while(!pool.isTerminated()) {
            try {
                VideoFrame f = service.take().get();
                frames.add(f);
                Bukkit.getLogger().info(Messages.PREFIX + "Rendered frame: " + f.getPosition());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }

        frames.sort(VideoFrame::compareTo);
        Video video = new Video(frames, name);
        Main.main.saveVideo(video);
        Main.main.videos.put(name, video);
        Bukkit.getLogger().info(Messages.RENDER_COMPLETE);
        sender.sendMessage(Messages.RENDER_COMPLETE);
    }

}
