package me.wertik.chunkclaims;

import me.wertik.chunkclaims.commands.MainCommand;
import me.wertik.chunkclaims.handlers.ClaimHandler;
import me.wertik.chunkclaims.handlers.CombatHandler;
import me.wertik.chunkclaims.listeners.ClaimListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    public static Main getInstance() {return instance;}

    private ConfigLoader configLoader;
    private ClaimHandler claimHandler;
    private DataHandler dataHandler;
    private CombatHandler combatHandler;

    @Override
    public void onEnable() {
        info("§f-------------");

        instance = this;
        configLoader = new ConfigLoader();
        claimHandler = new ClaimHandler();
        dataHandler = new DataHandler();
        combatHandler = new CombatHandler();
        getCommand("chunkclaims").setExecutor(new MainCommand());
        getServer().getPluginManager().registerEvents(new ClaimListener(), this);
        getServer().getPluginManager().registerEvents(new CombatHandler(), this);
        info("§aClasses loaded");

        configLoader.loadYamls();
        dataHandler.loadYamls();
        claimHandler.setPlayerClaims(dataHandler.loadClaims());
        claimHandler.startExpireSchedule();
        info("§aConfig loaded");

        info("§f-------------");
    }

    @Override
    public void onDisable() {
        dataHandler.saveClaims();
    }

    private void info(String msg) {
        getServer().getConsoleSender().sendMessage("§f["+getDescription().getName()+"] " + msg);
    }

    public ConfigLoader getConfigLoader() {
        return configLoader;
    }

    public ClaimHandler getClaimHandler() {
        return claimHandler;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    public CombatHandler getCombatHandler() {
        return combatHandler;
    }
}
