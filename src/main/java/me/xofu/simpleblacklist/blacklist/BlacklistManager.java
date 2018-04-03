package me.xofu.simpleblacklist.blacklist;

import me.xofu.simpleblacklist.SimpleBlacklist;
import me.xofu.simpleblacklist.file.ConfigFile;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlacklistManager {

    private SimpleBlacklist instance;

    private ConfigFile blacklistsFile;
    private Configuration blacklistsConfig;
    private List<Blacklist> blacklists;

    public BlacklistManager(SimpleBlacklist instance) {
        this.instance = instance;

        blacklistsFile = new ConfigFile("blacklists.yml", instance);
        blacklistsConfig = blacklistsFile.getConfig();
        blacklists = new ArrayList<>();

        load();
    }

    public void addBlacklist(Blacklist blacklist) {
        blacklists.add(blacklist);
    }

    public void removeBlacklist(Blacklist blacklist) {
        blacklists.remove(blacklist);
    }

    public boolean isBlacklisted(UUID uuid) {
        for(Blacklist blacklist: getBlacklists()) {
            if(blacklist.getPlayer().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public boolean isBlacklisted(String address) {
        for(Blacklist blacklist: getBlacklists()) {
            if(blacklist.getAddress() != null) {
                if (blacklist.getAddress().equals(address)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Blacklist getBlacklistByUUID(UUID uuid) {
        for(Blacklist blacklist: getBlacklists()) {
            if(blacklist.getPlayer().equals(uuid)) {
                return blacklist;
            }
        }
        return null;
    }

    public Blacklist getBlacklistByAddress(String address) {
        for(Blacklist blacklist: getBlacklists()) {
            if(blacklist.getAddress() != null) {
                if (blacklist.getAddress().equals(address)) {
                    return blacklist;
                }
            }
        }
        return null;
    }

    public List<Blacklist> getBlacklists() {
        if(blacklists == null) {
            return null;
        }
        return blacklists;
    }

    public void load() {
        for(String string: blacklistsConfig.getStringList("List.blacklists")) {
            UUID player = UUID.fromString(string);
            String punisher = blacklistsConfig.getString("Blacklist." + string + ".punisher");
            String reason = blacklistsConfig.getString("Blacklist." + string + ".reason");
            String address = blacklistsConfig.getString("Blacklist." + string + ".address") != null ? blacklistsConfig.getString("Blacklist." + string + ".address") : null;

            Blacklist blacklist = new Blacklist(player, punisher, reason);
            blacklist.setAddress(address);

            blacklists.add(blacklist);
        }
    }

    public void save() {
        List<String> blacklistList = new ArrayList<>();

        blacklistsConfig.set("Blacklist", null);
        blacklistsFile.save();
        for(Blacklist blacklist: getBlacklists()) {
            blacklistList.add(blacklist.getPlayer().toString());

            blacklistsConfig.set("Blacklist." + blacklist.getPlayer().toString() + ".punisher", blacklist.getPunisher());
            blacklistsConfig.set("Blacklist." + blacklist.getPlayer().toString() + ".reason", blacklist.getReason());
            blacklistsConfig.set("Blacklist." + blacklist.getPlayer().toString() + ".address", blacklist.getAddress() != null ? blacklist.getAddress() : null);

            blacklistsFile.save();
        }
        blacklistsConfig.set("List.blacklists", blacklistList);
        blacklistsFile.save();
    }
}
