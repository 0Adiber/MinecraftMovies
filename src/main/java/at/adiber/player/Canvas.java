package at.adiber.player;

import at.adiber.util.MapHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Canvas {

    private List<CanvasSection> sections;
    private BlockFace direction;
    private Location location;
    private UUID creator;
    private static final int PIXELS_PER_FRAME = 128;

    public Canvas(Location location, BlockFace direction, BufferedImage image, UUID creator) {
        sections = new ArrayList<>();
        this.creator = creator;
        this.location = location;
        this.direction = direction;
        this.update(image);
    }

    /**
     * Show or hide image for player if they are within 64 blocks
     * @param player The player to show image to
     * @param location The location of the player
     */
    public void refresh(Player player, Location location) {
        for(CanvasSection section : this.sections) {

            boolean sameWorld = location != null && section.getLocation().getWorld().equals(location.getWorld());
            if(sameWorld) {
                double distance = section.getLocation().distanceSquared(location);
                if(distance <= 64*64) {
                    section.show(player);
                } else {
                    section.hide(player);
                }
            } else {
                section.hide(player);
            }

        }
    }

    /**
     * Remove player from the shown; used if player leaves server
     * @param player The player to remove
     */
    public void remove(Player player) {
        for(CanvasSection section : sections) {
            section.shown.remove(player.getUniqueId());
        }
    }

    /**
     * Destroy all sections of the image and hide it from all players that it has been shown for.
     */
    public Set<Player> destroy() {
        Set<Player> players = new HashSet<>();
        for(CanvasSection section : sections) {
            section.shown.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).forEach(players::add);
            players.forEach(player -> MapHelper.destroyMap(player, section.getFrameId()));
        }
        this.sections.clear();
        return players;
    }

    /**
     * Change the image and show it
     * @param image
     */
    public void update(BufferedImage image) {
        if(image == null) return;
        Set<Player> players = this.destroy();
        BlockFace face;
        int rotation;
        switch (this.direction) {
            case UP:
            case DOWN:
                face = this.direction;
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
                throw new IllegalStateException("Invalid direction " + this.direction);
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

                CanvasSection section = new CanvasSection(this.direction,
                        rotation, loc, image.getSubimage(x * PIXELS_PER_FRAME, y * PIXELS_PER_FRAME,
                        PIXELS_PER_FRAME, PIXELS_PER_FRAME));
                this.sections.add(section);
            }
        }

        for (Player player : players) {
            this.refresh(player, player.getLocation());
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

}
