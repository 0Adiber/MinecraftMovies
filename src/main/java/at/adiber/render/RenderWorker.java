package at.adiber.render;

import at.adiber.player.VideoFrame;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.Callable;

public class RenderWorker implements Callable<VideoFrame> {

    private  BufferedImage img;
    private BlockFace direction;
    private Location location;
    private int position;

    public RenderWorker(BufferedImage img, BlockFace direction, Location location, int position) {
        this.img = img;
        this.direction = direction;
        this.location = location;
        this.position = position;
    }


    @Override
    public VideoFrame call() throws IOException {
        return new VideoFrame(direction, location, img, position);
    }
}