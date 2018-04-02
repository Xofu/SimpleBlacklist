package me.xofu.simpleblacklist;

import me.xofu.simpleblacklist.blacklist.BlacklistManager;
import me.xofu.simpleblacklist.commands.BlacklistCommand;
import me.xofu.simpleblacklist.commands.UnblacklistCommand;
import me.xofu.simpleblacklist.file.ConfigFile;
import me.xofu.simpleblacklist.listeners.BlacklistListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.util.concurrent.TimeUnit;

public class SimpleBlacklist extends Plugin {

    private ConfigFile configFile;

    private BlacklistManager blacklistManager;

    @Override
    public void onEnable() {
        configFile = new ConfigFile("config.yml", this);

        blacklistManager = new BlacklistManager(this);

        registerCommands();
        registerListeners();

        runSaveTask();
    }

    @Override
    public void onDisable() {
        blacklistManager.save();
    }

    public void registerCommands() {
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new BlacklistCommand(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new UnblacklistCommand(this));
    }

    public void registerListeners() {
        ProxyServer.getInstance().getPluginManager().registerListener(this, new BlacklistListener(this));
    }

    public BlacklistManager getBlacklistManager() {
        return blacklistManager;
    }

    public Configuration getConfig() {
        return configFile.getConfig();
    }

    public void runSaveTask() {
        getProxy().getScheduler().schedule(this, new Runnable() {
            @Override
            public void run() {
                blacklistManager.save();
            }
        }, 1, 300, TimeUnit.SECONDS);
    }
}
