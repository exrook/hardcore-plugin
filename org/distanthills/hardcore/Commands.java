package org.distanthills.hardcore;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.distanthills.hardcore.files.configFile;

import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String awd, String[] args) {
        if(command.getName().equalsIgnoreCase("hardcoreplugin")) {
            if(args.length == 0) {
                String message = "List of all Hazards: ";
                for(String s : configFile.getAvailableHazards()) {
                    if(configFile.getHazardStatus(s)) {
                        message += ChatColor.GREEN + s + ", ";
                    } else {
                        message += ChatColor.RED + s + ", ";
                    }
                }
                sender.sendMessage(message);
            }
            if(args.length == 1) {
                String arg = args[0].toLowerCase();
                if(configFile.getAvailableHazards().contains(arg)) {
                    if(configFile.getHazardStatus(arg)) {
                        sender.sendMessage(arg + " is currently turned " + ChatColor.GREEN + "ON");
                    } else {
                        sender.sendMessage(arg + " is currently turned " + ChatColor.RED + "OFF");
                    }
                    sender.sendMessage(configFile.getHazardDescription(args[0]));
                } else if(arg.equals("reload") && sender.isOp() || !(sender instanceof Player) || sender.hasPermission("hp.admin")) {
                    sender.sendMessage(ChatColor.GREEN + "Reloaded HardcorePlugin");
                    Main.get().getLogger().info("Reloaded the plugin!");
                    configFile.listEnabledHazards();
                    configFile.loadConfig();
                }
            } else if (args.length == 2) {
                if(sender.isOp() || !(sender instanceof Player) || sender.hasPermission("hp.admin")) {
                    String arg = args[0].toLowerCase();
                    String newStatus = args[1].toLowerCase();

                    if(configFile.getAvailableHazards().contains(arg)) {
                        if(newStatus.equals("on")) {
                            if(configFile.getHazardStatus(arg)) {
                                sender.sendMessage(ChatColor.GOLD + "That hazard is already enabled!");
                            } else {
                                configFile.toggle(arg);
                            }
                        } else if (newStatus.equals("off")) {
                            if(!configFile.getHazardStatus(arg)) {
                                sender.sendMessage(ChatColor.GOLD + "That hazard is already disabled!");
                            } else {
                                configFile.toggle(arg);
                            }
                        }
                    }
                }
            }
        }
        if(sender.isOp() || !(sender instanceof Player) || sender.hasPermission("hp.admin")) {
            if(configFile.getAvailableHazards().contains(command.getName())) {
                configFile.toggle(command.getName().toLowerCase());
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You do not have permission");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String aliases, String[] args) {
        if(command.getName().equalsIgnoreCase("hardcoreplugin")) {
            if(args.length == 1) {
                List<String> results = Lists.newArrayList();
                String arg = args[0].toLowerCase();
                for (String s1 : configFile.getAvailableHazards()) {
                    if (arg.equals("") || s1.startsWith(arg)) {
                        results.add(s1);
                    }
                }
                return results;
            } else if(args.length == 2) {
                List<String> results = Lists.newArrayList("ON", "OFF");
                if(configFile.getAvailableHazards().contains(args[0].toLowerCase())) {
                    return results;
                }
            }
        }
        return Lists.newArrayList();
    }
}
