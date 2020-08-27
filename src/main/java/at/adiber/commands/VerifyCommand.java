package at.adiber.commands;

import at.adiber.main.Main;
import at.adiber.util.Messages;
import at.adiber.util.Shared;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VerifyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage("You have to be a player!");
            return false;
        }

        Player player = (Player)sender;

        if(!player.hasPermission("movies.verify")) {
            player.sendMessage(Messages.NO_PERM);
            return false;
        }

        if(Main.main.bot.getUser(player.getUniqueId()) != null) {
            player.sendMessage(Messages.ALREADY_VERIFIED);
            return false;
        }

        String id;

        do {
            id = Shared.verifyIds.nextString();
        } while(Shared.verifyMap.get(id) != null);

        Shared.verifyMap.put(id, player.getUniqueId());
        player.sendMessage(String.format(Messages.VERIFY_ID, id));

        return true;
    }
}
