package de.mcgamer.guardian;

import com.google.gson.Gson;
import de.luckydev.luckyms.MySQLDatabase;
import de.luckydev.luckyms.MySQLException;
import de.luckydev.luckyms.MySQLService;
import de.mcgamer.guardian.commands.*;
import de.mcgamer.guardian.layout.BanTableLayout;
import de.mcgamer.guardian.listener.LoginListener;
import de.mcgamer.guardian.util.YamlConfigurationUtil;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Main extends Plugin {

    public static MySQLDatabase guardianDB;
    public static MySQLService mySQLService;

    public static Configuration config;

    public static Plugin plugin;

    public static final String PREFIX = "§7[§cGUARDIAN§7] §r";
    public static  final String NAME = "Guardian";
    public static  final String VERSION = "v1.0";

    public static Gson gson = new Gson();

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("§aEnabling §6" + NAME + " §aVersion: §6" + VERSION);

        //CONFIG LOADING
        //DELETE FROM `ban` WHERE `ban`.`uuid` = \'\'
        try {
            YamlConfigurationUtil.createDefault("config.yml");
            config = YamlConfigurationUtil.load("config.yml");
            YamlConfigurationUtil.saveConfig(config, "config.yml");
        } catch (IOException e) { e.printStackTrace(); }

        //MYSQL CONNECTING
        mySQLService = new MySQLService(config.getString("MySQL.host"), config.getInt("MySQL.port"), config.getString("MySQL.username"), config.getString("MySQL.password"));
        try {
            getLogger().info("§6Connecting with MySQL Service §a" + mySQLService.getURL() + "§6!");
            mySQLService.connect();
            guardianDB = mySQLService.createDBIfNotExists("guardian").connectToDB("guardian");
            guardianDB.createTableIfNotExists("ban", BanTableLayout.class);
        } catch (MySQLException exception) {
            getLogger().info("§c" + exception.getMessage());
        } finally {
            if(mySQLService.isConnected()) getLogger().info("§aSuccessfully Connected with " + mySQLService.getURL());
        }

        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerCommand(this, new BanCommand());
        pluginManager.registerCommand(this, new TBanCommand());
        pluginManager.registerCommand(this, new PBanCommand());
        pluginManager.registerCommand(this, new UnBanCommand());
        pluginManager.registerCommand(this, new KickCommand());

        pluginManager.registerListener(this, new LoginListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
