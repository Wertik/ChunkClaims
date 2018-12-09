package me.wertik.chunkclaims;

import me.wertik.chunkclaims.objects.ChunkLocation;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
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
            player.playEffect(chunk.getBlock(x, y, z).getLocation(), Effect.COLOURED_DUST, 14);
            x++;
        }
        for (int i = 0; i < 15; i++) {
            player.playEffect(chunk.getBlock(x, y, z).getLocation(), Effect.COLOURED_DUST, 14);
            z++;
        }
        for (int i = 0; i < 15; i++) {
            player.playEffect(chunk.getBlock(x, y, z).getLocation(), Effect.COLOURED_DUST, 14);
            x--;
        }
        for (int i = 0; i < 15; i++) {
            player.playEffect(chunk.getBlock(x, y, z).getLocation(), Effect.COLOURED_DUST, 14);
            z--;
        }
    }
}
