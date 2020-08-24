package at.adiber.commands;

import at.adiber.main.Main;
import at.adiber.player.Canvas;
import at.adiber.render.RenderManager;
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
            sender.sendMessage("Wrong arguments!");
            return false;
        }

        if(sender instanceof Player) {
            if(!((Player)sender).hasPermission("movies.render")) {
                sender.sendMessage("You have no Permissions to use that command!");
                return false;
            }
        }

        Canvas canvas = Main.main.canvases.get(args[0]);

        if(canvas == null) {
            sender.sendMessage("This canvas does not exist!");
            return false;
        }

        if(args[1].equalsIgnoreCase("folder")) {
            //Folder with images

            sender.sendMessage("Starting render... (see progress in console)");

            new BukkitRunnable(){
                @Override
                public void run() {
                    try {
                        new RenderManager(canvas.getLocation(), canvas.getBlockFace(), args[2])
                                .renderFromImages(Main.main.getDataFolder().getAbsolutePath() + File.separator + "movies" + File.separator + args[2]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        sender.sendMessage("Something went wrong (see console for more output)");
                    }
                }
            }.runTaskAsynchronously(Main.main);

        } else {
            //Video file

        }

        return true;
    }
}
