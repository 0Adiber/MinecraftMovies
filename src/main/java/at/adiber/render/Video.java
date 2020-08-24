package at.adiber.render;

import at.adiber.player.VideoFrame;

import java.util.List;

public class Video {
    private List<VideoFrame> frames;

    public Video(List<VideoFrame> frames) {
        this.frames = frames;
    }

    public Video() {}

    public void addFrame(VideoFrame frame) {
        this.frames.add(frame);
    }

    public List<VideoFrame> getFrames() {
        return this.frames;
    }
}
