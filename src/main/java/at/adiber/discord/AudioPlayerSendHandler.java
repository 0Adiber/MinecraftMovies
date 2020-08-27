package at.adiber.discord;

import at.adiber.main.Main;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer;
    private final MutableAudioFrame frame;

    /**
     * @param audioPlayer Audio player to wrap.
     */
    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(1024);
        this.frame = new MutableAudioFrame();
        this.frame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        // returns true if audio was provided
        return audioPlayer.provide(frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        // flip to make it a read buffer

        Main.main.bot.audioManager.currentVideoPlayer.setAudioLoaded(true);

        ((Buffer) buffer).flip();
        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}