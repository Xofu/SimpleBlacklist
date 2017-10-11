package me.xofu.blacklist.cmds;

import me.xofu.blacklist.Blacklist;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlacklistCommand implements CommandExecutor {

    private Blacklist instance;

    public BlacklistCommand(Blacklist instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String string, String[] args) {

        Player p = (Player) commandSender;

        if(p.hasPermission("simpleblacklist.use")) {
            if (args.length == 0) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("COMMAND_USAGE")));
                return true;
            }

            if(args[0].toLowerCase().equalsIgnoreCase(p.getName().toLowerCase())) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("CANT_BLACKLIST_YOURSELF")));
                return true;
            }

            if(instance.getFileManager().getBlacklists().getStringList("blacklisted-uuids").contains(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString())) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ALREADY_BLACKLISTED").replace("%player%", Bukkit.getOfflinePlayer(args[0]).getName())));
                return true;
            }

            if(Bukkit.getPlayerExact(args[0]) != null) {
                instance.getFileManager().uuids.add(Bukkit.getPlayerExact(args[0]).getUniqueId().toString());
                instance.getFileManager().getBlacklists().set("blacklisted-uuids", instance.getFileManager().uuids);
                String myString = "";
                for(int i = 0; i < args.length; i++){
                    String arg = args[i] + " ";
                    myString = myString + arg;
                }
                instance.getFileManager().getBlacklists().set("UUID." + Bukkit.getPlayerExact(args[0]).getUniqueId().toString() + ".reason", myString.replace(args[0], "").trim());
                instance.getFileManager().getBlacklists().set("UUID." + Bukkit.getPlayerExact(args[0]).getUniqueId().toString() + ".punisher", p.getName());
                instance.getFileManager().getBlacklists().set("UUID." + Bukkit.getPlayerExact(args[0]).getUniqueId().toString() + ".ip", Bukkit.getPlayerExact(args[0]).getAddress().getHostName());
                instance.getFileManager().saveBlacklists();

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("BLACKLISTED_PLAYER").replace("%player%", Bukkit.getPlayerExact(args[0]).getName()).replace("%punisher%", p.getName()).replace("%reason%", myString.replace(args[0], "").trim())));

                String kickMessage = "";
                for(String s: instance.getConfig().getStringList("kick-message")) {
                    kickMessage = kickMessage + "\n" + s;
                }
                Bukkit.getPlayerExact(args[0]).kickPlayer(ChatColor.translateAlternateColorCodes('&', kickMessage).replace("%punisher%", p.getName()).replace("%reason%", myString.replace(args[0], "").trim()));
                return true;
            }

            if(Bukkit.getOfflinePlayer(args[0]) != null) {
                instance.getFileManager().uuids.add(Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString());
                instance.getFileManager().getBlacklists().set("blacklisted-uuids", instance.getFileManager().uuids);
                String myString = "";
                for(int i = 0; i < args.length; i++){
                    String arg = args[i] + " ";
                    myString = myString + arg;
                }
                instance.getFileManager().getBlacklists().set("UUID." + Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString() + ".reason", myString.replace(args[0], "").trim());
                instance.getFileManager().getBlacklists().set("UUID." + Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString() + ".punisher", p.getName());
                instance.getFileManager().getBlacklists().set("UUID." + Bukkit.getOfflinePlayer(args[0]).getUniqueId().toString() + ".ip", "waiting");
                instance.getFileManager().saveBlacklists();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("BLACKLISTED_PLAYER").replace("%player%", Bukkit.getOfflinePlayer(args[0]).getName()).replace("%punisher%", p.getName()).replace("%reason%", myString.replace(args[0], "").trim())));
                return true;
            }
        }
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")));
        return false;
    }
}
