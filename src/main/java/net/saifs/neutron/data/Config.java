package net.saifs.neutron.data;

import com.google.common.base.Charsets;
import net.saifs.neutron.Neutron;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Config {
    private final Neutron plugin;

    private final String name;
    private File file;
    private FileConfiguration config;

    public Config(String name, Neutron plugin) {
        if (!name.toLowerCase().endsWith(".yml")) {
            name += ".yml";
        }

        this.name = name;
        this.plugin = plugin;

        this.load(plugin.getResource(name));
    }

    private void load(InputStream stream) {
        try {
            file = new File(this.plugin.getDataFolder() + "/" + name);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            config = YamlConfiguration.loadConfiguration(file);
            if (stream != null) {
                config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(stream, Charsets.UTF_8)));
                config.options().copyDefaults(true);
            }
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
