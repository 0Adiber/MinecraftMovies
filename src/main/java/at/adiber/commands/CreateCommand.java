package at.adiber.commands;

import at.adiber.main.Main;
import at.adiber.player.Canvas;
import at.adiber.player.VideoPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        Canvas canvas = new Canvas();
        canvas.addWatcher(player);

        VideoPlayer video = new VideoPlayer(canvas, Main.main.getDataFolder().getAbsolutePath() + File.separator + "movies" + File.separator + "test", player.getFacing().getOppositeFace(), player.getLocation());

        video.render();
        //BufferedImage img = ImageIO.read(new File(Movies.movies.getDataFolder(), "413.png"));
        //Canvas canvas = new Canvas(player.getLocation(), player.getFacing().getOppositeFace(), img, player.getUniqueId());
        //canvas.update(video.next());

        player.sendMessage("Started Render!");
        return true;
    }
}
