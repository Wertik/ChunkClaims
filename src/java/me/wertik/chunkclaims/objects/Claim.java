package me.wertik.chunkclaims.objects;

import me.wertik.chunkclaims.Main;

public class Claim {

    private String ownerUUID;
    private ChunkLocation chunkLocation;
    private long expireTime;

    public Claim(String ownerUUID, ChunkLocation chunkLocation, long expireTime) {
        this.ownerUUID = ownerUUID;
        this.chunkLocation = chunkLocation;
        this.expireTime = expireTime;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public ChunkLocation getChunkLocation() {
        return chunkLocation;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public boolean isActivated() {
        return !Main.getInstance().getCombatHandler().isInCombat(ownerUUID);
    }

    public void remove() {
        Main.getInstance().getClaimHandler().removeClaim(this);
    }
}
