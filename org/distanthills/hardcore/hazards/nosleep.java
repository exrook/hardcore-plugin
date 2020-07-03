package org.distanthills.hardcore.hazards;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.distanthills.hardcore.Main;
import org.distanthills.hardcore.files.configFile;

public class nosleep implements Listener {
    @EventHandler
    public void onBedPlace(BlockPlaceEvent e) {
        if(e.getBlockPlaced().getBlockData() instanceof org.bukkit.block.data.type.Bed) {
            if (configFile.getHazardStatus("nosleep")) {
                new BukkitRunnable() {
                    public void run() {
                        Float dmg = (float) configFile.getFcConfig().getDouble("enabledHazards.nosleep.explosionPower", 4);
                        e.getBlock().getWorld().createExplosion(e.getBlock().getLocation(), dmg, true, true);
                    }
                }.runTask(Main.get());
            }
        }
    }
}
