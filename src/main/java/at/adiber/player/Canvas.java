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
    private Set<Player> watchers;

    public Canvas() {
        sections = new ArrayList<>();
        watchers = new HashSet<>();
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

            /*
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
            }*/
    }

    /**
     * Remove player from the shown; used if player leaves server
     * @param player The player to remove
     */
    public void remove(Player player) {
        watchers.remove(player);
        for(CanvasSection section : sections) {
            section.shown.remove(player.getUniqueId());
        }
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

        for(int i = 0; i<this.sections.size(); i++) {
            this.destroy(this.sections.get(i));
            this.refresh(sections.get(i));
        }
        this.sections = new ArrayList<>(sections);

    }

}
