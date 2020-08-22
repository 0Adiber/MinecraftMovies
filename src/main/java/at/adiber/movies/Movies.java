package at.adiber.movies;

import at.adiber.commands.CreateCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Movies extends JavaPlugin {

    public static Movies movies;

    @Override
    public void onEnable() {
        movies = this;
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
    }

}
