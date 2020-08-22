package at.adiber.commands;

import at.adiber.movies.Movies;
import at.adiber.player.Canvas;
import at.adiber.player.VideoPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bytedeco.javacv.FrameGrabber;

import java.io.File;

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

        Canvas canvas = new Canvas(player.getLocation(), player.getFacing(), null, player.getUniqueId());

        VideoPlayer video = new VideoPlayer(new File(Movies.movies.getDataFolder(), "movies/cap20.mp4").getAbsolutePath(), canvas);
        try {
            video.start();
            canvas.refresh(player, player.getLocation());
            canvas.update(video.next());
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }

        player.sendMessage("Successfully created a new Canvas!");
        return true;
    }
}
