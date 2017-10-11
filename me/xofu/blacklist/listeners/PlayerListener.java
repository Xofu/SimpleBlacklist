package me.xofu.blacklist.listeners;

import me.xofu.blacklist.Blacklist;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerListener implements Listener {

    private Blacklist instance;

    public PlayerListener(Blacklist instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        if(!instance.getFileManager().getBlacklists().getStringList("blacklisted-uuids").contains(p.getUniqueId().toString())) {

            for(String s: instance.getFileManager().getBlacklists().getStringList("blacklisted-uuids")) {
                if(e.getAddress().getHostName().equals(instance.getFileManager().getBlacklists().getString("UUID." + s + ".ip"))) {

                    String kickMessage = "";
                    for(String st: instance.getConfig().getStringList("join-message")) {
                        kickMessage = kickMessage + "\n" + st;
                    }

                    kickMessage = ChatColor.translateAlternateColorCodes('&', kickMessage.replace("%punisher%", instance.getFileManager().getBlacklists().getString("UUID." + s + ".punisher")).replace("%reason%", instance.getFileManager().getBlacklists().getString("UUID." + s + ".reason")));

                    e.disallow(PlayerLoginEvent.Result.KICK_BANNED, kickMessage);
                }
            }
            System.out.println(e.getAddress().getHostName());
            return;
        }

        String kickMessage = "";
        for(String s: instance.getConfig().getStringList("join-message")) {
            kickMessage = kickMessage + "\n" + s;
        }

        kickMessage = ChatColor.translateAlternateColorCodes('&', kickMessage.replace("%punisher%", instance.getFileManager().getBlacklists().getString("UUID." + p.getUniqueId().toString() + ".punisher")).replace("%reason%", instance.getFileManager().getBlacklists().getString("UUID." + p.getUniqueId().toString() + ".reason")));

        e.disallow(PlayerLoginEvent.Result.KICK_BANNED, kickMessage);

    }
}
