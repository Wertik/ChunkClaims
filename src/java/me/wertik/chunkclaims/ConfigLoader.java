package me.wertik.chunkclaims;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class ConfigLoader {

    private Main plugin;
    private FileConfiguration config;

    public ConfigLoader() {
        plugin = Main.getInstance();
    }

    public void loadYamls() {

        // CF
        File configFile = new File(plugin.getDataFolder() + "/config.yml");

        if (!configFile.exists()) {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
            plugin.getServer().getConsoleSender().sendMessage(plugin.getDescription().getPrefix() + "§aGenerated default §f" + configFile.getName());
        }

        config = plugin.getConfig();
    }
}
