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

public class BlacklistCommand extends Command {

    private SimpleBlacklist instance;

    public BlacklistCommand(SimpleBlacklist instance) {
        super("blacklist");
        this.instance = instance;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("simpleblacklist.blacklist")) {
            sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("NO_PERMISSION")))));
            return;
        }

        if(args.length == 0) {
            sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("USAGE").replace("%usage%", "/blacklist <player> <reason>")))));
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

        if(instance.getBlacklistManager().isBlacklisted(uuid)) {
            sender.sendMessage(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("ALREADY_BLACKLISTED")))));
            return;
        }

        String reason = args.length == 1 ? String.join(" ", args).replace(args[0], "") : String.join(" ", args).replace(args[0] + " ", "");

        Blacklist blacklist = new Blacklist(uuid, sender.getName(), reason);
        instance.getBlacklistManager().addBlacklist(blacklist);

        ProxyServer.getInstance().broadcast(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', instance.getConfig().getString("PLAYER_BLACKLISTED").replace("%player%", args[0]).replace("%punisher%", sender.getName())))));

        if(ProxyServer.getInstance().getPlayer(args[0]) == null) {
            return;
        }

        instance.getBlacklistManager().removeBlacklist(instance.getBlacklistManager().getBlacklistByUUID(uuid));
        blacklist.setAddress(ProxyServer.getInstance().getPlayer(args[0]).getAddress().getHostName());
        instance.getBlacklistManager().addBlacklist(blacklist);

        String message = String.join("\n", instance.getConfig().getStringList("BLACKLISTED"));
        ProxyServer.getInstance().getPlayer(args[0]).disconnect(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', message.replace("%punisher%", blacklist.getPunisher()).replace("%reason%", blacklist.getReason())))));
    }
}
