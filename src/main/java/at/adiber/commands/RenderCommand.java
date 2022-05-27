package at.adiber.commands;

import at.adiber.main.Main;
import at.adiber.player.Canvas;
import at.adiber.player.VideoFrame;
import at.adiber.render.RenderConsumer;
import at.adiber.render.RenderProducer;
import at.adiber.util.Messages;
import at.adiber.util.Shared;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

            ExecutorService pool = Executors.newFixedThreadPool(Shared.Config.getRenderThreads());
            CompletionService<VideoFrame> service = new ExecutorCompletionService<>(pool);

            RenderProducer prod = new RenderProducer(canvas.getLocation(), canvas.getBlockFace(), service, Paths.get(Main.main.getDataFolder().getAbsolutePath(), "movies", args[2]), pool);
            RenderConsumer con = new RenderConsumer(service, sender, args[2], pool);

            Thread pThread = new Thread(prod);
            Thread cThread = new Thread(con);

            pThread.start();
            cThread.start();

        }

        return true;
    }
}
