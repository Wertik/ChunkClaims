package me.wertik.chunkclaims.listeners;

import me.wertik.chunkclaims.Main;
import me.wertik.chunkclaims.Utils;
import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ToolListener implements Listener {

    List<String> claimingPlayers = new ArrayList<>();

    public ToolListener() {
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getPlayer().getItemInHand() != null)
            if (e.getPlayer().getItemInHand().hasItemMeta())
                if (Main.getInstance().getToolHandler().isTool(e.getPlayer().getItemInHand())) {
                    e.setCancelled(true);
                    if (!Main.getInstance().getClaimHandler().isClaimed(e.getPlayer().getLocation())) {
                        if (Main.getInstance().getClaimHandler().getPlayerClaims(e.getPlayer().getUniqueId().toString()).size() < Main.getInstance().getConfigLoader().config.getInt("global-options.max-claims")) {

                            e.getPlayer().sendMessage("§cCCS: §fStarting to §cclaim§f, do not §cleave§f the chunk or get in §ccombat§f with another player for next §c" + Main.getInstance().getConfigLoader().config.getInt("tool.claim-time"));

                            if (!claimingPlayers.contains(e.getPlayer().getUniqueId().toString()))
                                claimingPlayers.add(e.getPlayer().getUniqueId().toString());
                            else {
                                e.getPlayer().sendMessage("§cAlready claiming!");
                                return;
                            }

                            new BukkitRunnable() {
                                Player player = e.getPlayer();
                                Chunk chunk = player.getLocation().getChunk();
                                int seconds = 0;

                                public void run() {
                                    if (!claimingPlayers.contains(player.getUniqueId().toString())) {
                                        player.sendMessage("§cStopped claiming..");
                                        cancel();
                                    } else if (Main.getInstance().getCombatHandler().isInCombat(player.getUniqueId().toString())) {
                                        player.sendMessage("§cGot in combat, aborting..");
                                        claimingPlayers.remove(player.getUniqueId().toString());
                                        cancel();
                                    } else if (!player.getLocation().getChunk().equals(chunk)) {
                                        player.sendMessage("§cYou left the chunk,.. abort.");
                                        claimingPlayers.remove(player.getUniqueId().toString());
                                        cancel();
                                    } else {
                                        seconds++;
                                        Utils.highlightClaim(chunk, player);
                                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
                                    }

                                    if (seconds == Main.getInstance().getConfigLoader().config.getInt("tool.claim-time")) {
                                        player.sendMessage("§cClaimed!");
                                        claimingPlayers.remove(player.getUniqueId().toString());
                                        Main.getInstance().getClaimHandler().claimChunk(player);
                                        Utils.highlightClaim(chunk, player);
                                        e.getPlayer().getInventory().setItemInMainHand(Main.getInstance().getToolHandler().takeUse(e.getPlayer().getItemInHand()));
                                        if (Main.getInstance().getToolHandler().getUses(e.getPlayer().getItemInHand()) == 0)
                                            e.getPlayer().setItemInHand(null);
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(Main.getInstance(), 20, 20);

                        } else
                            e.getPlayer().sendMessage("§cWayyy too many claims.");
                    } else {
                        if (Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(e.getPlayer().getLocation())).getOwnerUUID().equals(e.getPlayer().getUniqueId().toString()))
                            e.getPlayer().sendMessage("§cIt is already yours..");
                        else
                            e.getPlayer().sendMessage("§cAlready claimed..");
                    }
                }
    }

}
