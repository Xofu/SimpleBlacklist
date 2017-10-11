package me.xofu.blacklist.file;

import me.xofu.blacklist.Blacklist;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private Blacklist instance;

    public List<String> uuids = new ArrayList<>();

    private File blacklists;
    private FileConfiguration blacklistsConfig;

    public FileManager(Blacklist instance) {
        this.instance = instance;

        if(instance != null) {
            blacklists = new File(instance.getDataFolder(), "blacklists.yml");
            blacklistsConfig = YamlConfiguration.loadConfiguration(blacklists);
        }
    }

    public FileConfiguration getBlacklists() {
        return blacklistsConfig;
    }

    public void saveBlacklists() {
        try {
            blacklistsConfig.save(blacklists);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
