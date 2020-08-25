package at.adiber.render;

import at.adiber.player.VideoFrame;

import java.io.Serializable;
import java.util.List;

public class Video implements Serializable {
    private List<VideoFrame> frames;
    private String name;

    public Video(List<VideoFrame> frames, String name) {
        this.name = name;
        this.frames = frames;
    }

    public Video() {}

    public void addFrame(VideoFrame frame) {
        this.frames.add(frame);
    }

    public List<VideoFrame> getFrames() {
        return this.frames;
    }
    public String getName() {
        return this.name;
    }
}
