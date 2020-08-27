package at.adiber.player;

import at.adiber.util.MapHelper;
import jdk.nashorn.internal.codegen.MapCreator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class Canvas implements Serializable {

    private transient List<CanvasSection> sections; //this is dynamic, so saving it does not make sense
    private transient Set<Player> watchers; //does not have to be saved
    private BlockFace blockFace;
    private transient Location location;
    private String id;

    public Canvas(BlockFace blockFace, Location location, String id) {
        sections = new ArrayList<>();
        watchers = new HashSet<>();
        this.blockFace = blockFace;
        this.location = location;
        this.id = id;
    }

    public void addWatcher(Player player) {
        watchers.add(player);
    }

    /**
     * Show or hide a section for all watchers
     */
    public void refresh(CanvasSection section) {

        for(Player player : watchers) {
            section.show(player);
        }

    }

    /**
     * Remove player from the shown; used if player leaves server
     * @param player The player to remove
     */
    public void remove(Player player) {
        watchers.remove(player);
    }

    /**
     * Destroy a sections for all watchers
     */
    public void destroy(CanvasSection section) {

        for(Player player : watchers) {
            MapHelper.destroyMap(player, section.getFrameId());
        }

    }

    public void destroyAll() {

        if(this.sections == null)
            return;

        for(CanvasSection section : this.sections) {
            for(Player player : watchers) {
                MapHelper.destroyMap(player, section.getFrameId());
            }
        }

    }

    /**
     * Change the image and show it
     */
    public void update(List<CanvasSection> sections) {

        if(this.sections == null)
            this.sections = new ArrayList<>(sections);

        for(int i = 0; i<this.sections.size(); i++) {
            this.destroy(this.sections.get(i));
            CanvasSection section = sections.get(i);
            section.setDirection(this.blockFace);
            section.calcLocation(this.location);
            section.setMapId(this.location.getWorld());
            this.refresh(section);
        }
        this.sections = new ArrayList<>(sections);

    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public Location getLocation() {
        return location;
    }

    public String getId(){return id;}

    public static void writeLocation(ObjectOutputStream out, Location location) throws IOException {
        out.writeObject(location.getWorld().getName());
        out.writeInt(location.getBlockX());
        out.writeInt(location.getBlockY());
        out.writeInt(location.getBlockZ());
        out.writeFloat(location.getYaw());
        out.writeFloat(location.getPitch());
    }

    public static Location readLocation(ObjectInputStream in) throws IOException, ClassNotFoundException {

        String name = (String) in.readObject();
        World world = Bukkit.getWorld(name);
        return new Location(
                world,
                in.readInt(), in.readInt(), in.readInt(),
                in.readFloat(), in.readFloat()
        );
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        writeLocation(out, this.location);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.location = readLocation(in);
        watchers = new HashSet<>();
    }
}
