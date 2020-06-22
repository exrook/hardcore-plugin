package org.distanthills.hardcore;

import java.util.Comparator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class HardcorePlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("E");
        getServer().getPluginManager().registerEvents(this, this);
    }
    @Override
    public void onDisable() {
        getLogger().info("EA");
    }
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        getLogger().info("oof: " + p + " did this. " + event.getDeathMessage());
        regenWorlds();
        for (Player pp : this.getServer().getOnlinePlayers()) {
            pp.kickPlayer(p.getName() + "did this");
        }
        p.kickPlayer("get FRICKED");
    }
    public void regenWorlds() {
        this.regenWorld(this.getServer().getWorld("world"));
        this.regenWorld(this.getServer().getWorld("world_nether"));
        this.regenWorld(this.getServer().getWorld("world_the_end"));
        this.getServer().shutdown();
    }
    public void regenWorld(World world) {
        getLogger().info("DELETING world:" + world);
        File file = world.getWorldFolder();
        world.setAutoSave(false);
        this.getServer().unloadWorld(world, true);
        getLogger().info("Unloaded");
        try {
            Files.walk(file.toPath()).sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .filter(f -> !f.equals(file))
                        .forEach(File::delete);
        } catch (IOException e) {
            getLogger().info("POG");
        }
    }
}
