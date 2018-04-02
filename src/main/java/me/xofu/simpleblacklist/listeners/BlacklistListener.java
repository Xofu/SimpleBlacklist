package me.xofu.simpleblacklist.listeners;

import me.xofu.simpleblacklist.SimpleBlacklist;
import me.xofu.simpleblacklist.blacklist.Blacklist;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BlacklistListener implements Listener {

    private SimpleBlacklist instance;

    public BlacklistListener(SimpleBlacklist instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onLogin(LoginEvent event) {
        if(!instance.getBlacklistManager().isBlacklisted(event.getConnection().getUniqueId()) && !instance.getBlacklistManager().isBlacklisted(event.getConnection().getAddress().getHostName())) {
            return;
        }

        Blacklist blacklist = null;

        if(instance.getBlacklistManager().isBlacklisted(event.getConnection().getUniqueId())) {
            blacklist = instance.getBlacklistManager().getBlacklistByUUID(event.getConnection().getUniqueId());

            if(blacklist.getAddress() == null) {
                instance.getBlacklistManager().removeBlacklist(blacklist);
                blacklist.setAddress(event.getConnection().getAddress().getHostName());
                instance.getBlacklistManager().addBlacklist(blacklist);
            }
        }

        if(instance.getBlacklistManager().isBlacklisted(event.getConnection().getAddress().getHostName())) {
            blacklist = instance.getBlacklistManager().getBlacklistByAddress(event.getConnection().getAddress().getHostName());
        }

        event.setCancelled(true);

        String message = String.join("\n", instance.getConfig().getStringList("CURRENTLY_BLACKLISTED"));
        event.setCancelReason(ChatColor.translateAlternateColorCodes('&', message.replace("%punisher%", blacklist.getPunisher()).replace("%reason%", blacklist.getReason())));
    }
}
