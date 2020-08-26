package at.adiber.discord;

import at.adiber.util.Messages;
import at.adiber.util.Shared;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.UUID;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(event.getAuthor().equals(event.getJDA().getSelfUser())) return;

        String msg = event.getMessage().getContentRaw();

        if(!msg.startsWith(Shared.Config.getDc().getPrefix())) return;

        msg = msg.substring(Shared.Config.getDc().getPrefix().length());
        String[] args = msg.split(" ");

        if(event.getChannel().getType() == ChannelType.PRIVATE) {

            if(args.length == 0) {
                //TODO: Return help page
                event.getChannel().sendMessage("temporary").queue();
                return;
            }

            if(args[0].equalsIgnoreCase("verify")) {
                if(args.length < 2) {
                    event.getChannel().sendMessage(Messages.NO_VERIFY_CODE).queue();
                    return;
                }

                UUID uuid = Shared.verifyMap.get(args[1]);

                if(uuid == null) {
                    event.getChannel().sendMessage(String.format(Messages.WRONG_VERIFY_CODE, args[1])).queue();
                    return;
                }

                //TODO: Do verification STUFF!!!

                String player = "PLAYERNAME";

                System.out.println("new verified user: " + player);
                event.getChannel().sendMessage(String.format(Messages.VERIFY_SUCCESS, player)).queue();
                Shared.verifyMap.remove(args[1]);
            }
        }

    }
}
