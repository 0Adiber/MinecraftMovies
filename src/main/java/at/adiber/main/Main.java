package at.adiber.main;

import at.adiber.commands.CreateCommand;
import at.adiber.commands.RenderCommand;
import at.adiber.commands.StartCommand;
import at.adiber.player.Canvas;
import at.adiber.player.VideoPlayer;
import at.adiber.render.RenderManager;
import at.adiber.render.Video;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    public static Main main;
    public HashMap<String, Video> videos = new HashMap<>();
    public HashMap<String, Canvas> canvases = new HashMap<>();
    public HashMap<String, VideoPlayer> players = new HashMap<>();

    public List<RenderManager> renderer = new ArrayList<>();

    @Override
    public void onEnable() {
        main = this;
        getLogger().info("Minecraft Movies loaded.. happy watching!");
        init();
        registerCommands();
    }

    @Override
    public void onDisable() {
        for(RenderManager man : renderer) {
            getLogger().info("Terminating RenderManager...");
            try {
                man.terminate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        getLogger().info("Minecraft Movies successfully disabled");
    }

    public void init() {
        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        File folder = new File(getDataFolder(), "movies");
        if(!folder.exists())
            folder.mkdir();
    }

    public void registerCommands() {
        getCommand("cc").setExecutor(new CreateCommand());
        getCommand("start").setExecutor(new StartCommand());
        getCommand("render").setExecutor(new RenderCommand());
    }

}
