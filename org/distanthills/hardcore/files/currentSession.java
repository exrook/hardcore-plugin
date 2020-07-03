package org.distanthills.hardcore.files;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.distanthills.hardcore.Main;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.bukkit.Bukkit.getLogger;

public class currentSession {
    File thisSession = new File(Main.get().getDataFolder(), "currentStats.yml");
    FileConfiguration fcNow;

    static HashMap<String, Double> damageTaken = new HashMap<>();
    static Long duration;
    static Long startTime;

    public currentSession() {
        if(!thisSession.exists()) {
            try {
                thisSession.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fcNow = YamlConfiguration.loadConfiguration(thisSession);

        if(fcNow.contains("GameFinished")) {
            getLogger().info("Found New Game");
            thisSession.delete();
            try {
                thisSession.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fcNow = YamlConfiguration.loadConfiguration(thisSession);

        if(fcNow.contains("duration")) {
            duration = fcNow.getLong("duration");
        } else {
            duration = (long) 0;
        }

        if(fcNow.contains("DamageTaken")) {
            ConfigurationSection conf = fcNow.getConfigurationSection("DamageTaken");
            for (String name : conf.getKeys(false)) {
                damageTaken.put(name, conf.getDouble(name));
            }
        }
    }

    public void saveFile() {
        try {
            fcNow.save(thisSession);
        } catch (IOException e) {
            new IOException("Something went wrong whilst saving the current sessions file").printStackTrace();
        }
    }

    public void finishGame() {
        fcNow.set("GameFinished", true);
    }

    public void updateDamageTaken(String name, Double dmg) {
        damageTaken.put(name, damageTaken.getOrDefault(name, 0.0) + dmg);
    }

    public void resumeGame() {
        startTime = Calendar.getInstance().getTimeInMillis();
        getLogger().info("Resuming Logging Game Duration");
    }

    public void incrementDuration() {
        duration += Calendar.getInstance().getTimeInMillis() - startTime;
        startTime = Calendar.getInstance().getTimeInMillis();
        getLogger().info("Stopping Game Duration Timer");
    }

    public void fileSetDuration() {
        fcNow.set("duration", duration);
    }

    public void fileSetDamageTaken() {
        Iterator hmIterator = damageTaken.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) hmIterator.next();
            fcNow.set("DamageTaken." + mapElement.getKey(), mapElement.getValue());
        }
    }

    static public Long getDuration() {
        return duration;
    }

    static public Long getStartTime() {
        return startTime;
    }

    static public HashMap getDamageTaken() { return damageTaken; }
}
