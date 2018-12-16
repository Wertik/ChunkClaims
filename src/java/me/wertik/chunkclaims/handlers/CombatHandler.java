package me.wertik.chunkclaims.handlers;

import org.bukkit.event.Listener;

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

    public void addCombat(String uniqueID, long expireTime) {
        combatTimers.put(uniqueID, expireTime);
    }
}

