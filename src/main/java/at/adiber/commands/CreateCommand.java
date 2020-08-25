package at.adiber.commands;

import at.adiber.main.Main;
import at.adiber.player.Canvas;
import at.adiber.util.Messages;
import at.adiber.util.Shared;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.NOT_A_PLAYER);
            return false;
        }

        Player player = (Player)sender;
        if(!sender.hasPermission("movies.cc")) {
            player.sendMessage(Messages.NO_PERM);
            return false;
        }

        String id;
        do {
            id = Shared.genID();
        } while(Main.main.canvases.get(id) != null);

        Canvas canvas = new Canvas(player.getFacing().getOppositeFace(), player.getLocation(), id);
        Main.main.saveCanvas(canvas);
        Main.main.canvases.put(id, canvas);

        sender.sendMessage(String.format(Messages.ID_OF_CANVAS, id));

        canvas.addWatcher(player);

        player.sendMessage(Messages.CANVAS_CREATED);
        return true;
    }
}
