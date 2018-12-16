package me.wertik.chunkclaims.listeners;

import me.wertik.chunkclaims.Main;
import me.wertik.chunkclaims.Utils;
import me.wertik.chunkclaims.objects.Claim;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class ClaimListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        if (Main.getInstance().getClaimHandler().isClaimed(e.getBlock().getLocation())) {
            Claim claim = Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(e.getBlock().getLocation()));

            // Protect
            if (!claim.getOwnerUUID().equals(player.getUniqueId().toString()))
                if (claim.isActivated()) {
                    player.sendMessage("§cElse's territory, cannot build.");
                    e.setCancelled(true);
                }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if (Main.getInstance().getClaimHandler().isClaimed(e.getBlock().getLocation())) {
            Claim claim = Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(e.getBlock().getLocation()));

            // Protect
            if (!claim.getOwnerUUID().equals(player.getUniqueId().toString()))
                if (claim.isActivated()) {
                    player.sendMessage("§cElse's territory, cannot break.");
                    e.setCancelled(true);
                }
        }
    }
}
