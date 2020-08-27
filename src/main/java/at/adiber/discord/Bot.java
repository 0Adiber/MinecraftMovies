package at.adiber.discord;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Bot {
    JDABuilder builder;
    JDA api;

    public final AudioPlayerManager playerManager;
    public GuildAudioManager audioManager;

    private final Map<UUID, Long> playerUser;

    public Bot(String token) {
        this.playerUser = new HashMap<>();

        builder = JDABuilder.createDefault(token)
            .addEventListeners(new ReadyListener(), new MessageListener())
            .setAutoReconnect(true);

        builder.setActivity(Activity.streaming("Adiber is great", "https://adiber.rocks"));

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);

        try {
            api = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

    }

    public void setWatching(String movie) {
        api.getPresence().setActivity(Activity.watching(movie));
    }

    public void resetActivity() {
        api.getPresence().setActivity(Activity.streaming("Adiber is great", "https://adiber.rocks")); //won't show, because its not a valid streaming URL
    }

    public void shutdown() {
        api.shutdownNow();
    }

    public GuildAudioManager getGuildAudioPlayer(Guild guild) {
        if (audioManager == null) {
            audioManager = new GuildAudioManager(playerManager, guild);
        }

        guild.getAudioManager().setSendingHandler(audioManager.getSendHandler());

        return audioManager;
    }

    public JDA getApi() {
        return api;
    }

    public Long getUser(UUID uuid) {
        return playerUser.get(uuid);
    }

    public void putUser(UUID uuid, Long userId) {
        playerUser.put(uuid, userId);
    }

    public boolean hasUser(Long userId) { return playerUser.containsValue(userId); }

    public GuildAudioManager getAudioManager() {
        return audioManager;
    }
}