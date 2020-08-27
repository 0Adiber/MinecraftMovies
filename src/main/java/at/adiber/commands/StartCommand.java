package at.adiber.commands;

import at.adiber.main.Main;
import at.adiber.player.Canvas;
import at.adiber.player.VideoPlayer;
import at.adiber.render.Video;
import at.adiber.util.Messages;
import at.adiber.util.Shared;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length != 2) {
            sender.sendMessage(String.format(Messages.WRONG_ARGS, "/start CANVAS#ID VIDEO#NAME"));
            return false;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.NOT_A_PLAYER);
            return false;
        }

        Player player = (Player)sender;

        if(!player.hasPermission("movies.play")) {
            player.sendMessage(Messages.NO_PERM);
            return false;
        }

        if(Main.main.bot.getUser(player.getUniqueId()) == null) {
            player.sendMessage(Messages.NEED_TO_VERIFY);
            return false;
        }

        Canvas canvas = Main.main.canvases.get(args[0]);
        Video video = Main.main.videos.get(args[1]);

        if(canvas == null || video == null) {
            sender.sendMessage(Messages.CV_NOT_EXIST);
            return false;
        }

        for(Player all : Bukkit.getOnlinePlayers()) {
            canvas.addWatcher(all);
        }

        String id;
        do {
            id = Shared.genID();
        } while(Main.main.players.get(id) != null);

        VideoPlayer videoPlayer = new VideoPlayer(canvas, video, id);

        Main.main.players.put(id, videoPlayer);

        sender.sendMessage(String.format(Messages.ID_OF_PLAYER, id));

        videoPlayer.start(player.getUniqueId());

        return true;
    }

}
