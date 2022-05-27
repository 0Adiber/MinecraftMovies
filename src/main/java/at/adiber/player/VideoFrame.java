package at.adiber.player;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoFrame implements Comparable<VideoFrame>, Serializable {

    public List<CanvasSection> sections;
    private static final int PIXELS_PER_FRAME = 128;
    private int position;

    public VideoFrame(BufferedImage image, int position) {
        this.position = position;
        sections = new ArrayList<>();
        BlockFace face;


        int xSections = Math.max(image.getWidth() / PIXELS_PER_FRAME, 1);
        int ySections = Math.max(image.getHeight() / PIXELS_PER_FRAME, 1);
        //image = resize(image, xSections, ySections);
        for (int x = 0; x < xSections; x++) {

            for (int y = 0; y < ySections; y++) {

                CanvasSection section = new CanvasSection(image.getSubimage(x * PIXELS_PER_FRAME, y * PIXELS_PER_FRAME,
                        PIXELS_PER_FRAME, PIXELS_PER_FRAME), (byte)x, (byte)y);

                this.sections.add(section);
            }
        }
    }

    public static BufferedImage resize(BufferedImage image, int xSections, int ySections) {
        if (image.getWidth() % PIXELS_PER_FRAME == 0 && image.getHeight() % PIXELS_PER_FRAME == 0) {
            return image;
        }
        // Get a scaled version of the image
        Image img = image.getScaledInstance(xSections * PIXELS_PER_FRAME, ySections * PIXELS_PER_FRAME, Image.SCALE_DEFAULT);

        image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Copy the image over to the new instance
        Graphics2D g2D = image.createGraphics();
        g2D.drawImage(img, 0, 0, null);
        g2D.dispose();
        return image;
    }


    public List<CanvasSection> getSections() {
        return sections;
    }

    public Integer getPosition() {
        return position;
    }

    @Override
    public int compareTo(VideoFrame o) {
        return this.getPosition().compareTo(o.getPosition());
    }
}
