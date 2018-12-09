package me.wertik.chunkclaims.handlers;

import me.wertik.chunkclaims.Main;
import me.wertik.chunkclaims.Utils;
import me.wertik.chunkclaims.objects.ChunkLocation;
import me.wertik.chunkclaims.objects.Claim;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ClaimHandler {

    private static List<Claim> playerClaims;

    public ClaimHandler() {
        playerClaims = new ArrayList<>();
    }

    public void claimChunk(Player player) {
        Chunk chunk = player.getLocation().getChunk();

        Claim claim = new Claim(player.getUniqueId().toString(), new ChunkLocation(chunk.getWorld().getName(), chunk.getX(), chunk.getZ()), System.currentTimeMillis() + 360000);

        playerClaims.add(claim);
    }

    public List<Claim> getClaims() {
        return playerClaims;
    }

    public List<Claim> getPlayerClaims(String uniqueID) {
        List<Claim> outputList = new ArrayList<>();
        for (Claim claim : playerClaims) {
            if (claim.getOwnerUUID().equals(uniqueID)) {
                outputList.add(claim);
            }
        }
        return outputList;
    }

    public Claim getClaim(ChunkLocation chunkLocation) {
        for (Claim claim : playerClaims) {
            if (chunkLocation.compare(claim.getChunkLocation()))
                return claim;
        }
        return null;
    }

    public boolean isClaimed(Location location) {
        for (Claim claim : playerClaims) {
            ChunkLocation chunkLocation = Utils.createChunkLocation(location);
            if (chunkLocation.compare(claim.getChunkLocation()))
                return true;
        }
        return false;
    }

    public void removeClaim(Claim claim) {
        playerClaims.remove(claim);
    }

    public void startExpireSchedule() {
        new BukkitRunnable() {
            public void run() {
                Main.getInstance().getServer().getLogger().info("Looking for expired claims..");

                List<Claim> claimsToPurge = new ArrayList<>();

                for (Claim claim : playerClaims) {
                    if (claim.getExpireTime() < System.currentTimeMillis())
                        claimsToPurge.add(claim);
                }

                for (Claim claim : claimsToPurge) {
                    playerClaims.remove(claim);
                    Main.getInstance().getServer().getLogger().info("Purged expired claim..");
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 600);
    }

    public void setPlayerClaims(List<Claim> claims) {
        playerClaims.clear();
        playerClaims.addAll(claims);
    }
}
