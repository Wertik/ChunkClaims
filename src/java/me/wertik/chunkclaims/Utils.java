package me.wertik.chunkclaims;

import me.wertik.chunkclaims.objects.ChunkLocation;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Utils {

    public static ChunkLocation createChunkLocation(Location location) {
        return new ChunkLocation(location.getChunk().getWorld().getName(), location.getChunk().getX(), location.getChunk().getZ());
    }

    public static void highlightClaim(Chunk chunk, Player player) {
        for (int y = (int) player.getLocation().getY()-10; y < player.getLocation().getY()+10; y++) {
            highlightLevel(chunk, y, player);
        }
    }

    private static void highlightLevel(Chunk chunk, int y, Player player) {
        int z = 0;
        int x = 0;
        for (int i = 0; i < 15; i++) {
            playParticle(player.getWorld(), chunk, x, y, z);
            x++;
        }
        for (int i = 0; i < 15; i++) {
            playParticle(player.getWorld(), chunk, x, y, z);
            z++;
        }
        for (int i = 0; i < 15; i++) {
            playParticle(player.getWorld(), chunk, x, y, z);
            x--;
        }
        for (int i = 0; i < 15; i++) {
            playParticle(player.getWorld(), chunk, x, y, z);
            z--;
        }
    }

    private static void playParticle(World world, Chunk chunk, int x, int y, int z) {
        world.spawnParticle(Particle.REDSTONE, chunk.getBlock(x, y, z).getLocation(), 0, 1, 0, 0, 1);
    }

    public static String calculateTime(long start, long end) {
        long delta = end - start;
        if (delta < 0)
            return "expired";
        return String.valueOf(delta / 1000);
    }

    public static void fenceChunk(Chunk chunk) {

        Block block = chunk.getWorld().getHighestBlockAt(chunk.getBlock(0, 0, 0).getLocation());
        block.setType(Material.STAINED_CLAY);
        block.setData((byte) 14);

        block = chunk.getWorld().getHighestBlockAt(chunk.getBlock(0, 0, 15).getLocation());
        block.setType(Material.STAINED_CLAY);
        block.setData((byte) 14);

        block = chunk.getWorld().getHighestBlockAt(chunk.getBlock(15, 0, 15).getLocation());
        block.setType(Material.STAINED_CLAY);
        block.setData((byte) 14);

        block = chunk.getWorld().getHighestBlockAt(chunk.getBlock(15, 0, 0).getLocation());
        block.setType(Material.STAINED_CLAY);
        block.setData((byte) 14);
    }
}
