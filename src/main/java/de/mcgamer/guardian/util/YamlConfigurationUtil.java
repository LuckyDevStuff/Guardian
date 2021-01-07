package de.mcgamer.guardian.util;

import de.mcgamer.guardian.Main;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class YamlConfigurationUtil {
    public static ConfigurationProvider yamlProvider = ConfigurationProvider.getProvider(YamlConfiguration.class);

    public static void createDefault(String name) throws IOException {
        if (!Main.plugin.getDataFolder().exists())
            Main.plugin.getDataFolder().mkdir();

        File file = new File(Main.plugin.getDataFolder(), name);

        if (!file.exists()) {
            Files.copy(Main.plugin.getResourceAsStream(name), file.toPath());
        }
    }

    public static void saveConfig(Configuration config, String name) throws IOException {
        yamlProvider.save(config, new File(Main.plugin.getDataFolder(), name));
    }

    public static Configuration load(String name) throws IOException {
        return yamlProvider.load(new File(Main.plugin.getDataFolder(), name));
    }
}
