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
     * Show or hide image for player if they are within 64 blocks
     * @param player The player to show image to
     * @param location The location of the player
     */
    public void refresh(Player player, Location location) {
        for(CanvasSection section : this.sections) {

            if(watchers.contains(player)) {
                section.show(player);
            } else {
                section.hide(player);
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
        return players;
    }

    /**
     * Change the image and show it
     */
    public void update(List<CanvasSection> sections) {
        Set<Player> players = this.destroy();

        this.sections = new ArrayList<>(sections);

        for (Player player : watchers) {
            this.refresh(player, player.getLocation());
        }
    }

}
