package me.wertik.chunkclaims.listeners;

import me.wertik.chunkclaims.Main;
import me.wertik.chunkclaims.handlers.CombatHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CombatListener implements Listener {

    public CombatListener() {
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

        CombatHandler combatHandler = Main.getInstance().getCombatHandler();
        boolean messageTarget = true;
        boolean messageDamager = true;

        if (e.getDamager().getType().equals(EntityType.PLAYER) && e.getEntity().getType().equals(EntityType.PLAYER)) {

            Player target = (Player) e.getEntity();
            Player damager = (Player) e.getDamager();
            if (combatHandler.isInCombat(target.getUniqueId().toString()))
                messageTarget = false;
            if (combatHandler.isInCombat(damager.getUniqueId().toString()))
                messageDamager = false;

            if (Main.getInstance().getClaimHandler().isClaimed(e.getEntity().getLocation())) {
                combatHandler.addCombat(target.getUniqueId().toString(), System.currentTimeMillis() + Main.getInstance().getConfigLoader().config.getLong("global-options.combat-timer") * 1000);
                combatHandler.addCombat(damager.getUniqueId().toString(), System.currentTimeMillis() + Main.getInstance().getConfigLoader().config.getLong("global-options.combat-timer") * 1000);
                if (messageTarget)
                    target.sendMessage("§cYou're in combat now, your chunk's protection is disabled.");
                if (messageDamager)
                    damager.sendMessage("§cYou're in combat now, your chunk's protection is disabled.");
                return;

            } else {
                combatHandler.addCombat(target.getUniqueId().toString(), System.currentTimeMillis() + Main.getInstance().getConfigLoader().config.getLong("global-options.combat-timer") * 1000);
                combatHandler.addCombat(damager.getUniqueId().toString(), System.currentTimeMillis() + Main.getInstance().getConfigLoader().config.getLong("global-options.combat-timer") * 1000);
                if (messageTarget)
                    target.sendMessage("§cYou're in combat now, your chunk's protection is disabled.");
                if (messageDamager)
                    damager.sendMessage("§cYou're in combat now, your chunk's protection is disabled.");
            }
        }
    }
}
