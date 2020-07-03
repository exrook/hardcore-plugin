package org.distanthills.hardcore.files;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.distanthills.hardcore.Main;
import org.distanthills.hardcore.hazards.GlassCannon;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.bukkit.Bukkit.getLogger;

public class configFile {
    static File config = new File(Main.get().getDataFolder(), "config.yml");
    static FileConfiguration fcConfig;
    static String prepend = "enabledHazards.";

    static HashMap<String, String> availableHazards = new HashMap<>();
    static HashMap<String, Boolean> hazardStatus = new HashMap<>();

    public configFile() {

    }

    public static void put(String name, String desc) {
        availableHazards.put(name, desc);
    }

    public static void loadAvailableHazards() {
        put("coomer", "Makes a player coom on another player's punch");
        put("boomer", "Makes all enemies explode on death\nExplosion Power: " + fcConfig.getDouble("enabledHazards.boomer.explosionPower", 4) + "\nsetFire: " + fcConfig.getBoolean("enabledHazards.boomer.setFire", true) + "\nbreakBlocks: " + fcConfig.getBoolean("enabledHazards.boomer.breakBlocks", false));
        put("powerful", "Everything, including yourself, does " + fcConfig.getDouble("enabledHazards.powerful.damageModifier",2) + "x damage");
        put("glasscannon", "You can do " + fcConfig.getDouble("enabledHazards.glasscannon.damageModifier", 3) + "x damage, but you have " + fcConfig.getDouble("enabledHazards.glasscannon.maxHealth", 7) + " max hp");
        put("nosleep", "Beds will explode on placement.");
    }

    public static void loadConfig() {
        if(!config.exists()) {
            Main.get().saveResource("config.yml", false);
        }
        fcConfig = YamlConfiguration.loadConfiguration(config);

        loadAvailableHazards();

        Iterator hmIterator = availableHazards.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) hmIterator.next();
            hazardStatus.put((String) mapElement.getKey(), fcConfig.getBoolean(prepend + mapElement.getKey() + ".enabled", false));
        }
    }

    public static void saveConfig() {
        try {
            fcConfig.save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getFcConfig() {
        return fcConfig;
    }

    public static boolean getHazardStatus(String hazardName) {
        return hazardStatus.get(hazardName);
    }

    public static void listEnabledHazards() {
        getLogger().info("List of Hazards Enabled");
        Iterator hmIterator = hazardStatus.entrySet().iterator();
        while (hmIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) hmIterator.next();
            if((Boolean) mapElement.getValue()) {
                getLogger().info("" + mapElement.getKey());
            }
        }
        getLogger().info("...");
    }

    public static void toggle(String hazardName) {
        hazardStatus.put(hazardName, !hazardStatus.get(hazardName));
        fcConfig.set(prepend + hazardName, hazardStatus.get(hazardName));
        if(hazardStatus.get(hazardName)) {
            sendToggleMessages("Hazard " + hazardName + ": " + ChatColor.GREEN + "ON");
        } else {
            sendToggleMessages("Hazard " + hazardName + ": " + ChatColor.RED + "OFF");
        }

        hazardToggles();
    }

    public static void hazardToggles() { //specific behavior happens on toggle
        GlassCannon.toggle();
    }

    public static void sendToggleMessages(String message) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(message);
        }
    }

    public static List<String> getAvailableHazards() {
        Set<String> keySet = availableHazards.keySet();
        ArrayList<String> listOfKeys = new ArrayList<String>(keySet);
        return listOfKeys;
    }

    public static String getHazardDescription(String hazardName) {
        return availableHazards.get(hazardName);
    }
}
