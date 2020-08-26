package at.adiber.config;

import at.adiber.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {
    protected static final File file = new File(Main.main.getDataFolder(), "config.yml");
    protected static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public static Config load() {
        try {
            Config config = new Config();
            at.adiber.discord.Config dc = new at.adiber.discord.Config();

            config.setRenderThreads(cfg.getInt("render.threads"));

            dc.setPrefix(cfg.getString("discord.prefix"));
            dc.setToken(cfg.getString("discord.token"));
            dc.setMMRole(cfg.getString("discord.mmaster"));
            config.setDc(dc);

            return config;
        }catch(Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cConfig is not correct! Plugin will not be loaded!");
        }
        return null;
    }

    protected static void saveCfg() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}