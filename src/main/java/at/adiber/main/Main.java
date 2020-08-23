package at.adiber.main;

import at.adiber.commands.CreateCommand;
import at.adiber.commands.StartCommand;
import at.adiber.player.VideoPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public class Main extends JavaPlugin {

    public static Main main;
    public HashMap<String, VideoPlayer> videos = new HashMap<>();

    @Override
    public void onEnable() {
        main = this;
        getLogger().info("Minecraft Movies loaded.. happy watching!");
        init();
        registerCommands();
    }

    @Override
    public void onDisable() {
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
    }

}
