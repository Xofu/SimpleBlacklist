package me.xofu.blacklist;

import me.xofu.blacklist.cmds.BlacklistCommand;
import me.xofu.blacklist.file.FileManager;
import me.xofu.blacklist.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Blacklist extends JavaPlugin {

    private Blacklist instance;

    private FileManager fileManager;

    @Override
    public void onEnable() {
        instance = this;
        fileManager = new FileManager(this);

        if(fileManager.getBlacklists().getStringList("blacklisted-uuids") != null) {
            fileManager.uuids.addAll(fileManager.getBlacklists().getStringList("blacklisted-uuids"));
        }

        getCommand("blacklist").setExecutor(new BlacklistCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        saveDefaultConfig();
        super.onEnable();
    }

    public FileManager getFileManager() {
        return fileManager;
    }
}
