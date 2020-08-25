package at.adiber.render;

import at.adiber.io.IOAccess;
import at.adiber.main.Main;
import at.adiber.player.VideoFrame;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class RenderWorker implements Callable<VideoFrame> {

    private String folder;
    private BlockFace direction;
    private Location location;
    private int position;

    public RenderWorker(String folder, BlockFace direction, Location location, int position) {
        this.folder = folder;
        this.direction = direction;
        this.location = location;
        this.position = position;
    }


    @Override
    public VideoFrame call() throws IOException {
        BufferedImage img = IOAccess.readImages(folder, position);
        if(img == null) {
            throw new IOException("Render stopped");
        }
        return new VideoFrame(direction, location, img, position);
    }
}