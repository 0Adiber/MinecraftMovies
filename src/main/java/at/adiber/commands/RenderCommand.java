package at.adiber.commands;

import at.adiber.main.Main;
import at.adiber.player.Canvas;
import at.adiber.render.RenderManager;
import at.adiber.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class RenderCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length != 3) {
            sender.sendMessage(String.format(Messages.WRONG_ARGS, "/render CANVAS#ID TYPE FILE"));
            return false;
        }

        if(sender instanceof Player) {
            if(!((Player)sender).hasPermission("movies.render")) {
                sender.sendMessage(Messages.NO_PERM);
                return false;
            }
        }

        Canvas canvas = Main.main.canvases.get(args[0]);

        if(canvas == null) {
            sender.sendMessage(String.format(Messages.C_NOT_EXIST, args[0]));
            return false;
        }

        sender.sendMessage(Messages.RENDER_START);
        if(args[1].equalsIgnoreCase("folder")) {
            //Folder with images

            new BukkitRunnable(){
                @Override
                public void run() {
                    try {
                        new RenderManager(canvas.getLocation(), canvas.getBlockFace(), args[2], sender)
                                .renderFromImages(Main.main.getDataFolder().getAbsolutePath() + File.separator + "movies" + File.separator + args[2], true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        sender.sendMessage(Messages.UNKNOWN);
                    }
                }
            }.runTaskAsynchronously(Main.main);

        } else {
            //Video file

            new BukkitRunnable(){
                @Override
                public void run() {
                    try {
                        new RenderManager(canvas.getLocation(), canvas.getBlockFace(), args[2], sender)
                                .renderFromImages(Main.main.getDataFolder().getAbsolutePath() + File.separator + "movies" + File.separator + args[2] + ".mp4", false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        sender.sendMessage(Messages.UNKNOWN);
                    }
                }
            }.runTaskAsynchronously(Main.main);
        }

        return true;
    }
}
