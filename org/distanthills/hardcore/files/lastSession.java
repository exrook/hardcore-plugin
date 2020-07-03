package org.distanthills.hardcore.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.distanthills.hardcore.Main;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class lastSession {
    static File lastSession = new File(Main.get().getDataFolder(), "lastSessionStats.yml");
    static FileConfiguration fcLast;

    public lastSession() {

    }

    public static void saveStats(String deathReason) {
        if(!lastSession.exists()) {
            try {
                lastSession.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fcLast = YamlConfiguration.loadConfiguration(lastSession);

        Long duration = currentSession.getDuration();
        Long startTime = currentSession.getStartTime();
        HashMap<String, Double> damageTaken = currentSession.getDamageTaken();

        duration += Calendar.getInstance().getTimeInMillis() - startTime;

        double seconds = Math.floor(duration / 1000);
        double minutes = Math.floor(seconds / 60);
        double hours = Math.floor(minutes / 60);

        minutes = minutes - (hours * 60);
        seconds = seconds - (hours * 60 * 60) - (minutes * 60);

        int hour = (int) hours;
        int min = (int) minutes;
        int sec = (int) seconds;

        fcLast.set("GameDuration.seconds", sec);
        fcLast.set("GameDuration.minutes", min);
        fcLast.set("GameDuration.hours", hour);

        Iterator hmIterator = damageTaken.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) hmIterator.next();
            String name = (String) mapElement.getKey();
            fcLast.set("DamageTaken." + name, mapElement.getValue());
        }

        fcLast.set("DeathReason", deathReason);

        try {
            fcLast.save(lastSession);
        } catch (IOException e) {
            new IOException("Something went wrong whilst saving the last sessions file").printStackTrace();
        }
    }
}
