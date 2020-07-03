package org.distanthills.hardcore.files;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.distanthills.hardcore.Main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class globalStats {
    File globalStats = new File(Main.get().getDataFolder(), "globalStats.yml");
    FileConfiguration fcStats;

    HashMap<String, Double> globalDamageTaken = new HashMap<>();
    HashMap<String, Integer> globalDeaths = new HashMap<>();

    public globalStats() {
        if(!globalStats.exists()) {
            try {
                globalStats.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fcStats = YamlConfiguration.loadConfiguration(globalStats);

        if(fcStats.contains("Deaths")) {
            ConfigurationSection conf = fcStats.getConfigurationSection("Deaths");
            for (String name : conf.getKeys(false)) {
                globalDeaths.put(name, conf.getInt(name));
            }
        }

        if(fcStats.contains("DamageTaken")) {
            ConfigurationSection conf = fcStats.getConfigurationSection("DamageTaken");
            for (String name : conf.getKeys(false)) {
                globalDamageTaken.put(name, conf.getDouble(name));
            }
        }
    }

    public void saveFile() {
        Iterator gD = globalDeaths.entrySet().iterator();
        while (gD.hasNext()) {
            Map.Entry mapElement = (Map.Entry) gD.next();
            String name = (String) mapElement.getKey();
            fcStats.set("Deaths." + name, mapElement.getValue());
        }

        Iterator gDT = globalDamageTaken.entrySet().iterator();
        while (gDT.hasNext()) {
            Map.Entry mapElement = (Map.Entry) gDT.next();
            String name = (String) mapElement.getKey();
            fcStats.set("DamageTaken." + name, mapElement.getValue());
        }

        try {
            fcStats.save(globalStats);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addGlobalDamageTaken(String name, Double dmg) {
        globalDamageTaken.put(name, globalDamageTaken.getOrDefault(name, 0.0) + dmg);
    }

    public void addGlobalDeaths(String name, int i) {
        globalDeaths.put(name, globalDeaths.getOrDefault(name, 0) + i);
    }
}
