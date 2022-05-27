
package at.adiber.discord;

import at.adiber.main.Main;
import at.adiber.player.VideoPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import jdk.nashorn.internal.runtime.ECMAException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.concurrent.ConcurrentException;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildAudioManager {
    /**
     * Audio player for the guild.
     */
    public final AudioPlayer player;

    public final Guild guild;
    public VideoPlayer currentVideoPlayer;

    /**
     * Creates a player and a track scheduler.
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildAudioManager(AudioPlayerManager manager, Guild guild) {
        player = manager.createPlayer();
        this.guild = guild;
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }

    public AudioPlayer getPlayer(){
        return player;
    }

    public void loadAndPlay(final String trackUrl, String userId, VideoPlayer player) {

        Main.main.bot.playerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                play(track, userId, player);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                play(firstTrack, userId, player);
            }

            @Override
            public void noMatches() {
                //
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                //
            }
        });

    }

    private void play(AudioTrack track, String userId, VideoPlayer player) {
        this.currentVideoPlayer = player;
        try {
            connectToVoiceChannelOfUser(guild.getAudioManager(), userId);
        }catch (Exception e) {
            //TODO send sth to console and stuff
        }

        getPlayer().playTrack(track);
        //Main.main.bot.audioManager.currentVideoPlayer.setAudioLoaded(true);
    }

    public void disconnect() {
        guild.getAudioManager().closeAudioConnection();
    }

    private static void connectToVoiceChannelOfUser(AudioManager audioManager, String userId) throws Exception {
        if (!audioManager.isConnected()) {

            for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
                for(Member mem : voiceChannel.getMembers()) {
                    if(mem.getUser().getId().equals(userId)) {
                        audioManager.openAudioConnection(voiceChannel);
                        return;
                    }
                }
            }

            throw new Exception("User not found");

        }
        throw new Exception("Audioplayer already connected");
    }

    public Guild getGuild() {
        return guild;
    }
}