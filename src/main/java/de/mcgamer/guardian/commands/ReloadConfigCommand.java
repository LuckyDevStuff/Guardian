package de.mcgamer.guardian.commands;

import de.mcgamer.guardian.Main;
import de.mcgamer.guardian.util.YamlConfigurationUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class ReloadConfigCommand extends Command {
    public ReloadConfigCommand() {
        super("reloadconfig");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission(Main.config.getString("Permissions.Commands." + getName()))) {
            try {
                YamlConfigurationUtil.createDefault("config.yml");
                Main.config = YamlConfigurationUtil.load("config.yml");
                Main.setup();
            } catch (IOException e) { e.printStackTrace(); }
            sender.sendMessage(new TextComponent(Main.PREFIX + "§aThe config has been successfully reloaded!"));
        } else
            sender.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("Permissions.NoPermissionMessage")));
    }
}
