package at.adiber.commands;

import at.adiber.main.Main;
import at.adiber.player.VideoPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length != 1) {
            sender.sendMessage("To few arguments");
            return false;
        }

        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission("movies.play")) {
                player.sendMessage("You have no permissions!");
                return false;
            }
        }

        VideoPlayer player = Main.main.videos.get(args[0]);

        if(player == null) {
            sender.sendMessage("This video does not exist");
            return false;
        }

        player.start();

        return true;
    }
}
