package org.distanthills.hardcore.hazards;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.distanthills.hardcore.files.configFile;

public class Powerful implements Listener {
    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent e) {
        if(configFile.getHazardStatus("powerful")) {
            e.setDamage(e.getDamage() * configFile.getFcConfig().getDouble("enabledHazards.powerful.damageModifier", 2));
        }
    }
}
