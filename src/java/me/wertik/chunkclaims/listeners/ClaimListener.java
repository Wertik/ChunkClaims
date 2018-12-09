package me.wertik.chunkclaims.listeners;

import me.wertik.chunkclaims.Main;
import me.wertik.chunkclaims.Utils;
import me.wertik.chunkclaims.objects.Claim;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ClaimListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();

        if (Main.getInstance().getClaimHandler().isClaimed(e.getBlock().getLocation())) {
            Claim claim = Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(e.getBlock().getLocation()));

            // Protect
            if (!claim.getOwnerUUID().equals(player.getUniqueId().toString()))
            {
                player.sendMessage("Elses territory, cannot build.");
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
            {
                player.sendMessage("Elses territory, cannot break.");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType().equals(EntityType.PLAYER) && e.getDamager().getType().equals(EntityType.PLAYER)) {

            Player target = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();

            if (Main.getInstance().getClaimHandler().isClaimed(target.getLocation())) {
                Claim claim = Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(target.getLocation()));

                // Protect
                if (!claim.getOwnerUUID().equals(damager.getUniqueId().toString()) && claim.isActivated())
                {
                    damager.sendMessage("Elses territory, cannot break.");
                    e.setCancelled(true);
                }
            }
        }
    }
}
