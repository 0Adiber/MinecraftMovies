package at.adiber.render;

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

    private BufferedImage image;
    private BlockFace direction;
    private Location location;
    private int position;

    public RenderWorker(BufferedImage image, BlockFace direction, Location location, int position) {
        this.image = image;
        this.direction = direction;
        this.location = location;
        this.position = position;
    }


    @Override
    public VideoFrame call() throws Exception {

        try {
            return new VideoFrame(direction, location, image, position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        return null;
    }
}

final class RenderEntry<Integer, VideoFrame> implements Map.Entry<Integer, VideoFrame> {

    private final Integer key;
    private final VideoFrame value;

    public RenderEntry(Integer key, VideoFrame value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public VideoFrame getValue() {
        return value;
    }

    @Override
    public VideoFrame setValue(VideoFrame value) {
        return null;
    }
}
