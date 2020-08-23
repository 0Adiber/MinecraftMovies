package at.adiber.player;

import at.adiber.util.MapHelper;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CanvasSection {

    public static final int DEFAULT_STARTING_ID = Integer.MAX_VALUE / 4;
    private static final AtomicInteger ID_COUNTER = new AtomicInteger(DEFAULT_STARTING_ID);

    private final byte[] pixels;
    private final BlockFace direction;
    private final int rotation;

    private Location location;
    private int frameId, mapId;
    Set<UUID> shown = new HashSet<>();

    CanvasSection(BlockFace direction, int rotation, Location location, BufferedImage image) {
        this.direction = direction;
        this.rotation = rotation;
        this.location = location;
        this.pixels = MapHelper.getPixels(image);
        this.frameId = ID_COUNTER.getAndIncrement();
        this.mapId = MapHelper.nextMapId(location.getWorld());
    }

    /**
     * Get a copy of the pixel color array for this image section.
     * @return The pixels
     */
    public byte[] getPixels() {
        return pixels.clone();
    }

    /**
     * Get the direction is image is facing.
     * @return The direction
     */
    public BlockFace getDirection() {
        return direction;
    }

    /**
     * Get the amount of rotation this section is within it's own space (0-8)
     * @return The rotation
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Get the {@link Location} of this item frame section.
     * @return The frame location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Get the ID of the item frame that this section of the image is located in
     * @return The frame ID
     */
    public int getFrameId() {
        return frameId;
    }

    /**
     * Get the ID of the map that is located within the item frame
     * @return The map ID
     */
    public int getMapId() {
        return mapId;
    }

    /**
     * Show this image section to the player
     * @param player The player to show to.
     */
    public void show(Player player) {
        if(this.shown.add(player.getUniqueId())) {
            MapHelper.createMap(player, this.frameId, this.mapId, this.location, this.direction, this.rotation, this.pixels);
        }
    }

    /**
     * Clears the shown list. Needed to restart the video
     */
    public void clearShown() {
        this.shown.clear();
    }

    /**
     * Hide this image section from the player
     * @param player The player to hide from
     */
    public void hide(Player player) {
        if(this.shown.remove(player.getUniqueId())) {
            MapHelper.destroyMap(player, this.frameId);
        }
    }

}
