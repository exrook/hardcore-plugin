package org.distanthills.hardcore;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.scoreboard.*;
import org.distanthills.hardcore.files.*;
import org.distanthills.hardcore.hazards.*;

public class Main extends JavaPlugin implements Listener {
    Scoreboard healthboard;

    globalStats globalStatsFile;
    insults insultsFile;
    currentSession currentSessionFile;

    Commands commandHandler;

    public static Main in;

    public static Main get() {
        return in;
    }


    @Override
    public void onEnable() {
        in = this;
        getServer().getPluginManager().registerEvents(this, this);
        registerHazards();

        insultsFile = new insults();
        globalStatsFile = new globalStats();
        currentSessionFile = new currentSession();

        configFile.loadConfig();

        createHealthScoreboard();
        createTablistTimer();
        configFile.listEnabledHazards();

        commandHandler = new Commands();
        getCommand("hardcoreplugin").setExecutor(commandHandler);
        getCommand("hardcoreplugin").setTabCompleter(commandHandler);
    }

    public void registerHazards() {
        getServer().getPluginManager().registerEvents(new Coomer(), this);
        getServer().getPluginManager().registerEvents(new Boomer(), this);
        getServer().getPluginManager().registerEvents(new Powerful(), this);
        getServer().getPluginManager().registerEvents(new GlassCannon(), this);
        getServer().getPluginManager().registerEvents(new nosleep(), this);
    }

    @Override
    public void onDisable() {
        if(Bukkit.getOnlinePlayers().size() > 0) {
            currentSessionFile.incrementDuration();
        }
        currentSessionFile.fileSetDuration();
        currentSessionFile.fileSetDamageTaken();

        globalStatsFile.saveFile();
        currentSessionFile.saveFile();

        configFile.saveConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setScoreboard(healthboard);
        if(Bukkit.getOnlinePlayers().size() == 1) {
            currentSessionFile.resumeGame();
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        if(Bukkit.getOnlinePlayers().size() == 0) {
            currentSessionFile.incrementDuration();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntityType() == EntityType.PLAYER) {
            String name = event.getEntity().getName();

            currentSessionFile.updateDamageTaken(name, event.getFinalDamage());
            globalStatsFile.addGlobalDamageTaken(name, event.getFinalDamage());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        getLogger().info("oof: " + p.getName() + " did this. " + event.getDeathMessage());
        currentSessionFile.finishGame();

        globalStatsFile.addGlobalDeaths(p.getName(), 1);
        globalStatsFile.saveFile();

        configFile.saveConfig();

        Bukkit.getScheduler().runTask(this, () -> { // kicking must happen on next game tick
            for (Player pp : Bukkit.getOnlinePlayers()) {
                if(pp.equals(p)) {
                    continue;
                } else {
                    pp.kickPlayer(event.getDeathMessage());
                }
            }

            String insultmsg = insultsFile.getInsult();
            p.kickPlayer(insultmsg);

            lastSession.saveStats(event.getDeathMessage());
            regenWorlds();
        });
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

    public void createHealthScoreboard() {
        healthboard = getServer().getScoreboardManager().getMainScoreboard();
        Objective obj = healthboard.getObjective("Hearts2");
        if(obj == null) {
            obj = healthboard.registerNewObjective("Hearts2", Criterias.HEALTH, "Healthbars");
        }
        obj.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        obj.setRenderType(RenderType.HEARTS);


        Objective obj2 = healthboard.getObjective(("Hearts"));
        if(obj2 == null) {
            obj2 = healthboard.registerNewObjective("Hearts", Criterias.HEALTH, ChatColor.RED + "❤");
        }
        obj2.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }

    public void createTablistTimer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if(Bukkit.getOnlinePlayers().size() > 0) {
                double seconds = Math.floor((currentSessionFile.getDuration() + Calendar.getInstance().getTimeInMillis() - currentSessionFile.getStartTime()) / 1000);
                double minutes = Math.floor(seconds / 60);
                double hours = Math.floor(minutes / 60);

                minutes = minutes - (hours * 60);
                seconds = seconds - (hours * 60 * 60) - (minutes * 60);

                int hour = (int) hours;
                int min = (int) minutes;
                int sec = (int) seconds;

                String sHour = String.format("%02d", hour);
                String sMin = String.format("%02d", min);
                String sSec = String.format("%02d", sec);

                String header = ChatColor.GOLD + "Time Elapsed: " + ChatColor.WHITE + sHour + ":" + sMin + ":" + sSec;
                String footer = "";

                IChatBaseComponent b = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
                IChatBaseComponent c = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");

                PacketPlayOutPlayerListHeaderFooter d = new PacketPlayOutPlayerListHeaderFooter();
                d.header = b;
                d.footer = c;

                for (Player p : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(d);
                }
            }
        }, 0, 20);
    }
}
