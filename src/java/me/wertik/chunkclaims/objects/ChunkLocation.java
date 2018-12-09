package me.wertik.chunkclaims.objects;

import me.wertik.chunkclaims.Main;
import org.bukkit.Chunk;

public class ChunkLocation {

    private String worldName;
    private int x;
    private int z;

    public ChunkLocation(String worldName, int x, int z) {
        this.worldName = worldName;
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getWorldName() {
        return worldName;
    }

    public Chunk getChunk() {
        return Main.getInstance().getServer().getWorld(worldName).getChunkAt(x, z);
    }

    public String toString() {
        return x + ", " + z + " ; " + worldName;
    }

    public boolean compare(ChunkLocation chunkLocation) {
        if (!chunkLocation.getWorldName().equals(worldName))
            return false;
        if (!(chunkLocation.getX() == x))
            return false;
        if (!(chunkLocation.getZ() == z))
            return false;

        return true;
    }
}
