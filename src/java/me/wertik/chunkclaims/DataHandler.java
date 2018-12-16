package me.wertik.chunkclaims;

import me.wertik.chunkclaims.objects.ChunkLocation;
import me.wertik.chunkclaims.objects.Claim;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataHandler {

    private ConfigurationSection claimsSection;
    private YamlConfiguration claimsYaml;
    private File claimsFile;

    public void loadYamls() {
        claimsFile = new File(Main.getInstance().getDataFolder(), "claims.yml");

        if (!claimsFile.exists()) {
            claimsYaml = YamlConfiguration.loadConfiguration(claimsFile);
            Main.getInstance().saveResource("claims.yml", false);
            claimsYaml.createSection("claims");
            claimsYaml.options().copyDefaults(true);
            try {
                claimsYaml.save(claimsFile);
            } catch (IOException e) {
                Main.getInstance().getServer().getLogger().severe("Could not load claims.yml.");
                return;
            }
        } else
            claimsYaml = YamlConfiguration.loadConfiguration(claimsFile);

        claimsSection = claimsYaml.getConfigurationSection("claims");
        Main.getInstance().getServer().getLogger().info("Loaded claims.yml");
    }

    public void saveClaim(Claim claim, int id) {
        ConfigurationSection playerSection;

        if (!claimsSection.contains(claim.getOwnerUUID()))
            playerSection = claimsSection.createSection(claim.getOwnerUUID());
        else
            playerSection = claimsSection.getConfigurationSection(claim.getOwnerUUID());

        if (playerSection == null)
            Main.getInstance().getLogger().severe("playerSection == null");

        Main.getInstance().getLogger().info("ID: " + id);

        ConfigurationSection section = playerSection.createSection(String.valueOf(id));

        section.set("X", claim.getChunkLocation().getX());
        section.set("Z", claim.getChunkLocation().getZ());
        section.set("World", claim.getChunkLocation().getWorldName());

        section.set("Expire", claim.getExpireTime() - System.currentTimeMillis());
    }

    public void saveFile() {
        claimsYaml.options().copyDefaults(true);
        try {
            claimsYaml.save(claimsFile);
        } catch (IOException e) {
            Main.getInstance().getServer().getLogger().severe("Could not save claims.yml.");
        }
    }

    public void saveClaims() {
        claimsYaml.set("claims", null);
        claimsSection = claimsYaml.createSection("claims");
        int id = 0;
        for (Claim claim : Main.getInstance().getClaimHandler().getClaims()) {
            saveClaim(claim, id);
            id++;
        }
        saveFile();
    }

    public List<Claim> loadClaims() {

        List<Claim> outputList = new ArrayList<>();

        for (String playerSectionPath : claimsSection.getKeys(false)) {
            ConfigurationSection playerSection = claimsSection.getConfigurationSection(playerSectionPath);

            for (String sectionPath : playerSection.getKeys(false)) {
                ConfigurationSection section = playerSection.getConfigurationSection(sectionPath);

                ChunkLocation chunkLocation = new ChunkLocation(section.getString("World"), section.getInt("X"), section.getInt("Z"));

                outputList.add(new Claim(playerSectionPath, chunkLocation, section.getLong("Expire") + System.currentTimeMillis()));
            }
        }
        return outputList;
    }
}
