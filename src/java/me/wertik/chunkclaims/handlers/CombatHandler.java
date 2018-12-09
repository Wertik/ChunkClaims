package me.wertik.chunkclaims.handlers;

import me.wertik.chunkclaims.Main;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;

public class CombatHandler implements Listener {

    // Player UUID to String, combat expire time
    private HashMap<String, Long> combatTimers;

    public CombatHandler() {
        combatTimers = new HashMap<>();
    }

    public boolean isInCombat(String uniqueID) {
        if (combatTimers.containsKey(uniqueID)) {
            if (combatTimers.get(uniqueID) < System.currentTimeMillis()) {
                combatTimers.remove(uniqueID);
                return false;
            } else return true;
        } else return false;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager().getType().equals(EntityType.PLAYER) && e.getEntity().getType().equals(EntityType.PLAYER)) {
            combatTimers.put(e.getEntity().getUniqueId().toString(), System.currentTimeMillis()+1000);
            combatTimers.put(e.getDamager().getUniqueId().toString(), System.currentTimeMillis()+1000);
            e.getEntity().sendMessage("Combat timer..");
            e.getDamager().sendMessage("Combat timer..");
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (Main.getInstance().getClaimHandler().isClaimed(e.getBlock().getLocation())) {

        }
    }
}
