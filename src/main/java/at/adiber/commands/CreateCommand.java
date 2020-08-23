package at.adiber.commands;

import at.adiber.movies.Movies;
import at.adiber.player.Canvas;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CreateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player!");
            return false;
        }

        Player player = (Player)sender;
        if(!sender.hasPermission("movies.admin")) {
            player.sendMessage("You do not have permissions to use that command!");
            return false;
        }

        //Canvas canvas = new Canvas(player.getLocation(), player.getFacing(), null, player.getUniqueId());

        //VideoPlayer video = new VideoPlayer(new File(Movies.movies.getDataFolder(), "movies/cap20.mp4").getAbsolutePath(), canvas);

        try {
           // video.start();
            BufferedImage img = ImageIO.read(new File(Movies.movies.getDataFolder(), "413.png"));
            Canvas canvas = new Canvas(player.getLocation(), player.getFacing().getOppositeFace(), img, player.getUniqueId());
            canvas.refresh(player, player.getLocation());
            //canvas.update(video.next());
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.sendMessage("Successfully created a new Canvas!");
        return true;
    }
}
