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

    private YamlConfiguration claimsYaml;
    private File claimsFile;

    public void loadYamls() {
        claimsFile = new File(Main.getInstance().getDataFolder(), "claims.yml");

        if (!claimsFile.exists()) {
            claimsYaml = YamlConfiguration.loadConfiguration(claimsFile);
            Main.getInstance().saveResource("claims.yml", false);
            claimsYaml.options().copyDefaults(true);
            try {
                claimsYaml.save(claimsFile);
            } catch (IOException e) {
                Main.getInstance().getServer().getLogger().severe("Could not load claims.yml.");
                return;
            }
        } else
            claimsYaml = YamlConfiguration.loadConfiguration(claimsFile);
        Main.getInstance().getServer().getLogger().info("Loaded claims.yml");
    }

    public void saveClaim(Claim claim) {

        ConfigurationSection playerSection;
        if (claimsYaml.contains(claim.getOwnerUUID()))
            playerSection = claimsYaml.getConfigurationSection(claim.getOwnerUUID());
        else
            playerSection = claimsYaml.createSection(claim.getOwnerUUID());

        ConfigurationSection section = claimsYaml.getConfigurationSection(claim.getOwnerUUID()).createSection(String.valueOf(playerSection.getKeys(false).size()));

        section.set("X", claim.getChunkLocation().getX());
        section.set("Z", claim.getChunkLocation().getZ());
        section.set("World", claim.getChunkLocation().getWorldName());

        section.set("Expire", claim.getExpireTime());
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
        for (Claim claim : Main.getInstance().getClaimHandler().getClaims()) {
            // For Each.. ;)
            saveClaim(claim);
        }
        saveFile();
    }

    public List<Claim> loadClaims() {

        List<Claim> outputList = new ArrayList<>();

        for (String playerSectionPath : claimsYaml.getKeys(false)) {
            ConfigurationSection playerSection = claimsYaml.getConfigurationSection(playerSectionPath);

            for (String sectionPath : playerSection.getKeys(false)) {
                ConfigurationSection section = playerSection.getConfigurationSection(sectionPath);

                ChunkLocation chunkLocation = new ChunkLocation(section.getString("World"), section.getInt("X"), section.getInt("Z"));

                outputList.add(new Claim(playerSectionPath, chunkLocation, section.getLong("Expire"), true));
            }
        }
        return outputList;
    }
}
