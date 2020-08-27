package at.adiber.events;

import at.adiber.main.Main;
import at.adiber.player.Canvas;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventPlayerQuit implements Listener {

    @EventHandler
    public void on(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        for(Canvas c : Main.main.canvases.values()) {
            c.remove(player);
        }

    }

}
