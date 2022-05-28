package at.adiber.player;

import at.adiber.util.MapHelper;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CanvasSection implements Serializable {

    private static final long serialVersionUID = 1337133713371337L;

    public static final int DEFAULT_STARTING_ID = Integer.MAX_VALUE / 4;
    private static final AtomicInteger ID_COUNTER = new AtomicInteger(DEFAULT_STARTING_ID);

    private final byte[] pixels;
    private transient BlockFace direction; //will be calculated

    private transient Location location; //this will be calculated
    private transient int frameId, mapId; //should not be saved
    public transient Set<UUID> shown = new HashSet<>(); //should also not be saved

    private byte x,y;

    CanvasSection(BufferedImage image, byte x, byte y) {
        this.pixels = MapHelper.getPixels(image);
        this.frameId = ID_COUNTER.getAndIncrement();
        //this.mapId = MapHelper.nextMapId(world);
        this.x = x;
        this.y = y;
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
    public synchronized void show(Player player) {
        byte[] uncompressed = MapHelper.uncompress(this.pixels);
        if(this.shown.add(player.getUniqueId())) {
            MapHelper.createMap(player, this.frameId, this.mapId, this.location, this.direction, uncompressed);
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
    public synchronized void hide(Player player) {
        if(this.shown.remove(player.getUniqueId())) {
            MapHelper.destroyMap(player, this.frameId);
        }
    }

    public void calcLocation(Location location) {

        switch (direction) {
            case UP:
                this.location = location.clone().add(x,0,y);
                break;
            case DOWN:
                this.location = location.clone().add(x,0,-y);
                break;
            case NORTH:
                this.location = location.clone().add(-x,-y,0);
                break;
            case SOUTH:
                this.location = location.clone().add(x,-y,0);
                break;
            case EAST:
                this.location = location.clone().add(0,-y,-x);
                break;
            case WEST:
                this.location = location.clone().add(0,-y, x);
                break;
        }

    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.frameId = ID_COUNTER.getAndIncrement();
        this.shown = new HashSet<>();
    }

    public void setMapId(World world) {
        this.mapId = MapHelper.nextMapId(location);
    }

    public void setDirection(BlockFace direction) {
        this.direction = direction;
    }

}
