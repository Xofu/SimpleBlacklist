package me.xofu.simpleblacklist.commands;

import me.xofu.simpleblacklist.SimpleBlacklist;
import me.xofu.simpleblacklist.blacklist.Blacklist;
import me.xofu.simpleblacklist.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class UnblacklistCommand extends Command {

    private SimpleBlacklist instance;

    public UnblacklistCommand(SimpleBlacklist instance) {
        super("unblacklist");
        this.instance = instance;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("simpleblacklist.unblacklist")) {
            sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")))));
            return;
        }

        if(args.length == 0) {
            sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("USAGE").replace("%usage%", "/unblacklist <player>")))));
            return;
        }

        UUID uuid = null;
        try {
            uuid = UUIDFetcher.getUUIDOf(args[0]);
            if(uuid == null) {
                sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_A_PLAYER")))));
                return;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        if(!instance.getBlacklistManager().isBlacklisted(uuid)) {
            sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NOT_BLACKLISTED")))));
            return;
        }

        Blacklist blacklist = instance.getBlacklistManager().getBlacklistByUUID(uuid);
        instance.getBlacklistManager().removeBlacklist(blacklist);

        ProxyServer.getInstance().broadcast(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("PLAYER_UNBLACKLISTED").replace("%player%", args[0]).replace("%punisher%", sender.getName())))));
    }
}
