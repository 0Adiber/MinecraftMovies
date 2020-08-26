package at.adiber.config;

public class Config {

    private at.adiber.discord.Config dc;

    private int renderThreads;

    public Config(){}

    public at.adiber.discord.Config getDc() {
        return dc;
    }

    public void setDc(at.adiber.discord.Config dc) {
        this.dc = dc;
    }

    public int getRenderThreads() {
        return renderThreads;
    }

    public void setRenderThreads(int renderThreads) {
        this.renderThreads = renderThreads;
    }

}
