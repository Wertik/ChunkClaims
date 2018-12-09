package me.wertik.chunkclaims.commands;

import me.wertik.chunkclaims.Main;
import me.wertik.chunkclaims.Utils;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MainCommand implements CommandExecutor {

    // Todo particle effects

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage("/chunkclaims claim" +
                    "\n/chunkclaims info" +
                    "\n/chunkclaims debug" +
                    "\n/chunkclaims unclaim" +
                    "\n/chunkclaims show");
            return false;
        }

        // Claim a chunk
        // Todo time controlled claim
        if (args[0].equalsIgnoreCase("claim")) {
            if (Main.getInstance().getClaimHandler().isClaimed(p.getLocation()))
            {
                sender.sendMessage("Already claimed..");
                return false;
            }
            Main.getInstance().getClaimHandler().claimChunk(p);
            p.sendMessage("§cClaimed.. ?");

            // Debug info
            // Todo remove
        } else if (args[0].equalsIgnoreCase("debug")) {
            sender.sendMessage("Chunks.. : ");
            Main.getInstance().getClaimHandler().getClaims().forEach(claim -> sender.sendMessage(claim.getChunkLocation().toString()));

            // Info about a claim
        } else if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage("Chunk info: ");
            sender.sendMessage("X: " + p.getLocation().getChunk().getX() + " Z: " + p.getLocation().getChunk().getZ());
            sender.sendMessage("Claimed: " + Main.getInstance().getClaimHandler().isClaimed(p.getLocation()));

            if (Main.getInstance().getClaimHandler().isClaimed(p.getLocation())) {
                sender.sendMessage("OwnerUUID: " + Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(p.getLocation())).getOwnerUUID());
                sender.sendMessage("Expire time: " + Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(p.getLocation())).getExpireTime());
                sender.sendMessage("Activated: " + Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(p.getLocation())).isActivated());
            }

            // Remove your claim
        } else if (args[0].equalsIgnoreCase("unclaim")) {
            Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(p.getLocation())).remove();
            sender.sendMessage("Done.");

            // Show your claims -- particles
        } else if (args[0].equalsIgnoreCase("show")) {
            final Chunk chunk = p.getLocation().getChunk();
            final Player chunkPlayer = p;
            new BukkitRunnable() {
                int count = 0;
                public void run() {
                    if (count > 10)
                        cancel();
                    Utils.highlightClaim(chunk, chunkPlayer);
                    count++;
                }
            }.runTaskTimer(Main.getInstance(), 0, 20);
            p.sendMessage("Showed your current chunk.");
        } else if (args[0].equalsIgnoreCase("comabt")) {
            p.sendMessage("Combat status: " + Main.getInstance().getCombatHandler().isInCombat(p.getUniqueId().toString()));
        }

        return false;
    }
}
