package at.adiber.player;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class VideoFrame {

    public List<CanvasSection> sections;
    private static final int PIXELS_PER_FRAME = 128;

    public VideoFrame(BlockFace direction, Location location, BufferedImage image) {
        sections = new ArrayList<>();
        BlockFace face;
        int rotation;
        switch (direction) {
            case UP:
            case DOWN:
                face = direction;
                rotation = 0;
                break;
            case NORTH:
                face = BlockFace.SOUTH;
                rotation = 0;
                break;
            case SOUTH:
                face = BlockFace.NORTH;
                rotation = 0;
                break;
            case EAST:
                face = BlockFace.WEST;
                rotation = 0;
                break;
            case WEST:
                face = BlockFace.EAST;
                rotation = 0;
                break;
            default:
                throw new IllegalStateException("Invalid direction " + direction);
        }

        int xSections = Math.max(image.getWidth() / PIXELS_PER_FRAME, 1);
        int ySections = Math.max(image.getHeight() / PIXELS_PER_FRAME, 1);
        image = resize(image, xSections, ySections);
        for (int x = 0; x < xSections; x++) {

            for (int y = 0; y < ySections; y++) {

                Location loc = location.clone();
                switch (face) {
                    case UP:
                        loc.add(x, 0, y);
                        break;
                    case DOWN:
                        loc.add(x, 0, -y);
                        break;
                    case SOUTH:
                        loc.add(-x, -y, 0);
                        break;
                    case NORTH:
                        loc.add(x, -y, 0);
                        break;
                    case WEST:
                        loc.add(0, -y, -x);
                        break;
                    case EAST:
                        loc.add(0, -y, x);
                        break;
                }

                CanvasSection section = new CanvasSection(direction,
                        rotation, loc, image.getSubimage(x * PIXELS_PER_FRAME, y * PIXELS_PER_FRAME,
                        PIXELS_PER_FRAME, PIXELS_PER_FRAME));
                this.sections.add(section);
            }
        }
    }

    public static BufferedImage resize(BufferedImage image, int xSections, int ySections) {
        if (image.getWidth() % PIXELS_PER_FRAME == 0 && image.getHeight() % PIXELS_PER_FRAME == 0) {
            return image;
        }
        // Get a scaled version of the image
        Image img = image.getScaledInstance(xSections * PIXELS_PER_FRAME,
                ySections * PIXELS_PER_FRAME, Image.SCALE_DEFAULT);
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
}
