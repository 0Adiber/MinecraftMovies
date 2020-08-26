package at.adiber.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.security.auth.login.LoginException;

public final class Bot {
    JDABuilder builder;
    JDA api;

    public Bot(String token) {
        builder = JDABuilder.createDefault(token)
            .addEventListeners(new ReadyListener(), new MessageListener())
            .setAutoReconnect(true);

        builder.setActivity(Activity.watching("Adiber programming"));

        try {
            api = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

    }

    private void setWatching(String movie) {
        api.getPresence().setActivity(Activity.watching(movie));
    }

    private void joinChannel(Guild guild, String id) {
        VoiceChannel channel = guild.getVoiceChannelById(id);
        AudioManager audioManager = guild.getAudioManager();
        audioManager.openAudioConnection(channel);
    }

    public void shutdown() {
        api.shutdownNow();
    }

}