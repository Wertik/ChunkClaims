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
            sender.sendMessage("§8§m----§r §cChunkClaims Help Page §8§m----§r");
            sender.sendMessage("§c/ccs claim §8- §7Admin claim command." +
                    "\n§c/ccs help §8- §7This.." +
                    "\n§c/ccs info §8- §7Shows info about your chunks." +
                    "\n§c/ccs list §8- §7Lists your claimed chunks." +
                    "\n§c/ccs unclaim §8- §7Unclaim your claimchunk." +
                    "\n§c/ccs show §8- §7Highlight chunk you are standing in." +
                    "\n§c/ccs tool §8- §7Gives you the tool." +
                    "\n§c/ccs combat §8- §7Shows info about your combat status.");
            return false;
        }

        // Claim a chunk
        if (args[0].equalsIgnoreCase("claim")) {
            if (Main.getInstance().getClaimHandler().isClaimed(p.getLocation())) {
                sender.sendMessage("Already claimed..");
                return false;
            }
            Main.getInstance().getClaimHandler().claimChunk(p);
            p.sendMessage("§cClaimed.. ?");

            // Debug info
            // Todo remove
        } else if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage("§dChunk info: ");
            sender.sendMessage("§fX: §c" + p.getLocation().getChunk().getX() + " §fZ: §c" + p.getLocation().getChunk().getZ());
            sender.sendMessage("§fClaimed: §c" + Main.getInstance().getClaimHandler().isClaimed(p.getLocation()));

            if (Main.getInstance().getClaimHandler().isClaimed(p.getLocation())) {
                sender.sendMessage("§fOwnerUUID: §c" + Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(p.getLocation())).getOwnerUUID());
                sender.sendMessage("§fExpire time: §c" + Utils.calculateTime(System.currentTimeMillis(), Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(p.getLocation())).getExpireTime()));
                sender.sendMessage("§fActivated: §c" + Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(p.getLocation())).isActivated());
            }

            // Remove your claim
        } else if (args[0].equalsIgnoreCase("unclaim")) {
            if (Main.getInstance().getClaimHandler().isClaimed(p.getLocation())) {
                Main.getInstance().getClaimHandler().getClaim(Utils.createChunkLocation(p.getLocation())).remove();
                sender.sendMessage("§cDone.");
            } else
                sender.sendMessage("§cNot claimed.");

            // Show your claims -- particles
        } else if (args[0].equalsIgnoreCase("show")) {
            final Chunk chunk = p.getLocation().getChunk();
            final Player chunkPlayer = p;
            new BukkitRunnable() {
                int count = 0;

                public void run() {
                    if (count > 20)
                        cancel();
                    Utils.highlightClaim(chunk, chunkPlayer);
                    count++;
                }
            }.runTaskTimer(Main.getInstance(), 0, 20);
            p.sendMessage("§cShowed your current chunk.");
        } else if (args[0].equalsIgnoreCase("combat")) {
            p.sendMessage("§fCombat status: §c" + Main.getInstance().getCombatHandler().isInCombat(p.getUniqueId().toString()));
        } else if (args[0].equalsIgnoreCase("list")) {
            p.sendMessage("§dYour chunks:");
            Main.getInstance().getClaimHandler().getPlayerClaims(p.getUniqueId().toString()).forEach(claim -> p.sendMessage("§fX: §c" + claim.getChunkLocation().getX() +
                    " §fZ: §c" + claim.getChunkLocation().getZ() +
                    " §fTime left: §c" + Utils.calculateTime(System.currentTimeMillis(), claim.getExpireTime())));
        } else if (args[0].equalsIgnoreCase("tool")) {
            if (Main.getInstance().getConfigLoader().config.getBoolean("tool.enabled")) {
                p.sendMessage("§cGiven..!");
                p.getInventory().addItem(Main.getInstance().getToolHandler().createTool(Main.getInstance().getConfigLoader().config.getInt("tool.uses")));
            } else
                p.sendMessage("§cTools are not enabled..");
        } else if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage("§8§m----§r §cChunkClaims Help Page §8§m----§r");
            sender.sendMessage("§c/ccs claim §8- §7Admin claim command." +
                    "\n§c/ccs help §8- §7This.." +
                    "\n§c/ccs info §8- §7Shows info about your chunks." +
                    "\n§c/ccs list §8- §7Lists your claimed chunks." +
                    "\n§c/ccs unclaim §8- §7Unclaim your claimchunk." +
                    "\n§c/ccs show §8- §7Highlight chunk you are standing in." +
                    "\n§c/ccs tool §8- §7Gives you the tool." +
                    "\n§c/ccs combat §8- §7Shows info about your combat status.");
        } else {
            sender.sendMessage("§8§m----§r §cChunkClaims Help Page §8§m----§r");
            sender.sendMessage("§c/ccs claim §8- §7Admin claim command." +
                    "\n§c/ccs help §8- §7This.." +
                    "\n§c/ccs info §8- §7Shows info about your chunks." +
                    "\n§c/ccs list §8- §7Lists your claimed chunks." +
                    "\n§c/ccs unclaim §8- §7Unclaim your claimchunk." +
                    "\n§c/ccs show §8- §7Highlight chunk you are standing in." +
                    "\n§c/ccs tool §8- §7Gives you the tool." +
                    "\n§c/ccs combat §8- §7Shows info about your combat status.");

        }

        return false;
    }
}
