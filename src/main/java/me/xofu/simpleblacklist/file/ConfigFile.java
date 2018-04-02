package me.xofu.simpleblacklist.file;

import com.google.common.io.ByteStreams;
import me.xofu.simpleblacklist.SimpleBlacklist;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class ConfigFile {

    private SimpleBlacklist instance;

    public String name;
    public File file;
    private Configuration config;

    public ConfigFile(String name, SimpleBlacklist instance) {
        this.instance = instance;

        this.name = name;
        this.file = new File(instance.getDataFolder(), name);

        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();

            try {
                if (instance.getResourceAsStream(name) == null) {
                    this.file.createNewFile();
                } else {
                    InputStream is = instance.getResourceAsStream(name);
                    OutputStream os = new FileOutputStream(file);
                    ByteStreams.copy(is, os);
                    is.close();
                    os.close();
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfig() {
        return this.config;
    }

    public void save() {
        try {
            ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
            provider.save(getConfig(), file);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}
