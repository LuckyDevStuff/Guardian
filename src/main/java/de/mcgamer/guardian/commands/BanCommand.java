package de.mcgamer.guardian.commands;

import de.mcgamer.guardian.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.List;

public class BanCommand extends Command {
    public BanCommand() {
        super("ban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
            if(sender.hasPermission(Main.config.getString("Permissions." + getName()))) {
                sender.sendMessage(new TextComponent(Main.PREFIX + "§c!BETA! Please use /tban or /pban!"));
            } else
                sender.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("Permissions.NoPermissionMessage")));
    }
}
