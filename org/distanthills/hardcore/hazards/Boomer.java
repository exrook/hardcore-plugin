package org.distanthills.hardcore.hazards;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.distanthills.hardcore.files.configFile;

public class Boomer implements Listener {
    //hazard name: boomer
    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        if(configFile.getHazardStatus("boomer")) {
            Location loc = e.getEntity().getLocation();
            float power = (float) configFile.getFcConfig().getDouble("enabledHazards.boomer.explosionPower", 4);
            boolean setFire = configFile.getFcConfig().getBoolean("enabledHazards.boomer.setFire", true);
            boolean breakBlocks = configFile.getFcConfig().getBoolean("enabledHazards.boomer.breakBlocks", false);
            e.getEntity().getWorld().createExplosion(loc, power, setFire, breakBlocks);
        }
    }
}
