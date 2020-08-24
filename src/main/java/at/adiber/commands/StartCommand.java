package at.adiber.commands;

import at.adiber.main.Main;
import at.adiber.player.Canvas;
import at.adiber.player.VideoPlayer;
import at.adiber.render.Video;
import at.adiber.util.Shared;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;

public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length != 2) {
            sender.sendMessage("Wrong arguments");
            return false;
        }

        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission("movies.play")) {
                player.sendMessage("You have no permissions!");
                return false;
            }
        }

        Canvas canvas = Main.main.canvases.get(args[0]);
        Video video = Main.main.videos.get(args[1]);

        if(canvas == null || video == null) {
            sender.sendMessage("Canvas or Video does not exist");
            return false;
        }

        String id;
        do {
            id = Shared.genID();
        } while(Main.main.players.get(id) != null);

        VideoPlayer player = new VideoPlayer(canvas, video, id);

        Main.main.players.put(id, player);

        sender.sendMessage("Id of the Player: " + id);

        player.start();

        return true;
    }

}
