package de.mcgamer.guardian.commands;

import de.luckydev.luckyms.MySQLException;
import de.mcgamer.guardian.Main;
import de.mcgamer.guardian.util.UUIDUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class UnBanCommand extends Command {
    public UnBanCommand() {
        super("unban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission(Main.config.getString("Permissions." + getName()))) {
            //unban <Player>
            if(args.length == 1) {
                String uuid = UUIDUtil.getOfflinePlayer(args[0]).toString();
                if(uuid != null) {
                    try {
                        Main.guardianDB.getTable("ban").deleteAllWhereIs("uuid", uuid);
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§aSuccessfully un banned §6" + args[0] + "§a!"));
                    } catch (MySQLException exception) {
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player isn't banned"));
                    }
                } else
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player doesn't exist!"));
            } else
                sender.sendMessage(new TextComponent(Main.PREFIX + "§cPlease use §6/unban <Player>§c!"));
        } else
            sender.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("Permissions.NoPermissionMessage")));
    }
}
