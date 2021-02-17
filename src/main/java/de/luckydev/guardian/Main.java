package de.luckydev.guardian;

import com.google.gson.Gson;
import de.luckydev.guardian.commands.*;
import de.luckydev.guardian.listener.ChatListener;
import de.luckydev.luckyms.MySQLDatabase;
import de.luckydev.luckyms.MySQLException;
import de.luckydev.luckyms.MySQLService;
import de.mcgamer.guardian.commands.*;
import de.luckydev.guardian.layout.IPBanTableLayout;
import de.luckydev.guardian.layout.UUIDBanTableLayout;
import de.luckydev.guardian.listener.LoginListener;
import de.luckydev.guardian.util.UUIDUtil;
import de.luckydev.guardian.util.YamlConfigurationUtil;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public final class Main extends Plugin {

    public static MySQLDatabase guardianDB;
    public static MySQLService mySQLService;

    public static Configuration config;

    public static Plugin plugin;

    public static final String PREFIX = "§7[§cGUARDIAN§7] §r";
    public static  final String NAME = "Guardian";
    public static  final String VERSION = "v1.0";
    public static ArrayList<String> cantBan = new ArrayList<>();

    public static Gson gson = new Gson();

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("§aEnabling §6" + NAME + " §aVersion: §6" + VERSION);

        setup();

        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerCommand(this, new BanCommand());
        pluginManager.registerCommand(this, new TBanCommand());
        pluginManager.registerCommand(this, new PBanCommand());
        pluginManager.registerCommand(this, new UnBanCommand());
        pluginManager.registerCommand(this, new KickCommand());
        pluginManager.registerCommand(this, new ReloadConfigCommand());

        pluginManager.registerListener(this, new LoginListener());
        pluginManager.registerListener(this, new ChatListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void setup() {
        //CONFIG LOADING
        try {
            YamlConfigurationUtil.createDefault("config.yml");
            config = YamlConfigurationUtil.load("config.yml");
        } catch (IOException e) { e.printStackTrace(); }

        //Cant ban
        cantBan = new ArrayList<>();
        for(String string : config.getStringList("Ban.notBanAble")) {
            UUID uuid = UUIDUtil.getOfflinePlayer(string);
            if(uuid != null) {
                cantBan.add(uuid.toString());
            } else
                cantBan.add(string);
        }
        config.set("Ban.notBanAble", cantBan);
        try {
            YamlConfigurationUtil.saveConfig(config, "config.yml");
        } catch (Exception ignored) {}

        //MYSQL CONNECTING
        mySQLService = new MySQLService(config.getString("MySQL.host"), config.getInt("MySQL.port"), config.getString("MySQL.username"), config.getString("MySQL.password"));
        try {
            plugin.getLogger().info("§6Connecting with MySQL Service §a" + mySQLService.getURL() + "§6!");
            mySQLService.connect();
            guardianDB = mySQLService.createDBIfNotExists("guardian").connectToDB("guardian");
            guardianDB.createTableIfNotExists("uuid-ban", UUIDBanTableLayout.class);
            guardianDB.createTableIfNotExists("ip-ban", IPBanTableLayout.class);
        } catch (MySQLException exception) {
            plugin.getLogger().info("§c" + exception.getMessage());
        } finally {
            if(mySQLService.isConnected()) plugin.getLogger().info("§aSuccessfully Connected with " + mySQLService.getURL());
        }
    }
}
