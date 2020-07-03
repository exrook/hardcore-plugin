package org.distanthills.hardcore.hazards;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.distanthills.hardcore.files.configFile;

public class GlassCannon implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(configFile.getHazardStatus("glasscannon")) {
            Double baseHp = configFile.getFcConfig().getDouble("enabledHazards.glasscannon.maxHealth", 7);
            e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseHp);
        } else {
            e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
        }
    }

    @EventHandler
    public void onDamageEvent(EntityDamageByEntityEvent e) {
        if(configFile.getHazardStatus("glasscannon")) {
            if(e.getDamager() instanceof Player) {
                Double dmgMod = configFile.getFcConfig().getDouble("enabledHazards.glasscannon.damageModifier", 3);
                e.setDamage(e.getDamage() * dmgMod);
            }
        }
    }

    public static void toggle() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            Double baseHp = 20.0;
            if(configFile.getHazardStatus("glasscannon")) {
                baseHp = configFile.getFcConfig().getDouble("enabledHazards.glasscannon.maxHealth", 7);
            }
            p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseHp);
        }
    }
}
