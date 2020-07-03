package org.distanthills.hardcore.hazards;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.distanthills.hardcore.Main;
import org.distanthills.hardcore.files.configFile;

import java.util.List;
import java.util.Random;

public class Coomer implements Listener {
    //hazard name: coomer
    //COOMER
    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if(configFile.getHazardStatus("coomer")) {
            if(event.getDamager().getType() == EntityType.PLAYER && event.getEntityType() == EntityType.PLAYER) {
                Player p = (Player) event.getDamager();
                Player u = (Player) event.getEntity();
                p.chat("*strokes " + u.getDisplayName() + "*");


                Bukkit.getScheduler().runTaskLater(Main.get(), () -> {
                    u.chat("I-II... I'M GONNA COOOOOOOOOOOOOOOOOOOM");
                }, 40);

                Bukkit.getScheduler().runTaskLater(Main.get(), () -> {
                    ItemStack coom = new ItemStack(Material.MILK_BUCKET);
                    ItemMeta im = coom.getItemMeta();
                    im.setDisplayName("COOM");
                    List<String> lore = Lists.newArrayList(u.getDisplayName() + "'s coom");
                    im.setLore(lore);
                    coom.setItemMeta(im);

                    List<ItemStack> droppedItems = Lists.newArrayList();
                    Vector direction = u.getLocation().getDirection();
                    Random rand = new Random();
                    for(int i = 0; i < rand.nextInt(Bukkit.getOnlinePlayers().size()) + 1; i++) {
                        Item temp = u.getWorld().dropItem(u.getLocation(), coom);

                        Vector tDirection = direction;
                        temp.setVelocity(tDirection.multiply(rand.nextInt(150)/50));
                    }

                    u.getInventory().addItem(coom);

                    AreaEffectCloud coomCloud = (AreaEffectCloud) p.getWorld().spawnEntity(p.getLocation(), EntityType.AREA_EFFECT_CLOUD);
                    coomCloud.setColor(Color.WHITE);
                    coomCloud.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 1200, 0, true, true), false);
                    coomCloud.setDuration(200);
                    coomCloud.setDurationOnUse(0);
                    coomCloud.setRadius(3);
                    coomCloud.setRadiusOnUse(0);
                    coomCloud.setRadiusPerTick(0);
                    coomCloud.setReapplicationDelay(0);
                    coomCloud.setWaitTime(0);
                    coomCloud.setParticle(Particle.SPIT);
                }, 60);
            }
        }
    }

    //COOMER
    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if(configFile.getHazardStatus("coomer")) {
            if (event.getItem().getItemStack().getItemMeta().getDisplayName().equals("COOM")) {
                event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 1200, 0, true, true));
            }
        }
    }

    //COOMER
    @EventHandler
    public void conSOOM(PlayerItemConsumeEvent event) {
        if(configFile.getHazardStatus("coomer")) {
            if (event.getItem().getItemMeta().getDisplayName().equals("COOM")) {
                event.setCancelled(true);
                Player p = event.getPlayer();
                p.removePotionEffect(PotionEffectType.POISON);
                p.chat("*slurps " + event.getItem().getItemMeta().getLore().get(0) + "*");
                p.getInventory().remove(event.getItem());
            }
        }
    }
}
