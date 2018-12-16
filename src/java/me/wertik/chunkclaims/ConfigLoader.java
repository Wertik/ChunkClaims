package me.wertik.chunkclaims;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {

    private Main plugin;
    public FileConfiguration config;

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

    public String getCFString(String path) {
        return format(config.getString(path));
    }

    public String getCFString(String path, Player player) {
        return format(parseString(config.getString(path), player, 0));
    }

    public List<String> getCFList(String path) {
        List<String> outputList = new ArrayList<>();
        for (String line : config.getStringList(path)) {
            outputList.add(format(line));
        }
        return outputList;
    }

    private String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public String parseString(String msg, Player player, int uses) {
        if (player != null)
            if (msg.contains("%player%"))
                msg = msg.replace("%player%", player.getName());
        if (msg.contains("%uses%"))
            msg = msg.replace("%uses%", String.valueOf(uses));
        return msg;
    }

    public List<String> parseList(List<String> msg, Player player, int uses) {
        for (String line : msg) {
            msg.set(msg.indexOf(line), format(parseString(line, player, uses)));
        }
        return msg;
    }
}
