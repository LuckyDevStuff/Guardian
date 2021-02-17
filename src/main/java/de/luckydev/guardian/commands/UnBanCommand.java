package de.luckydev.guardian.commands;

import de.luckydev.luckyms.MySQLException;
import de.luckydev.guardian.Main;
import de.luckydev.guardian.util.UUIDUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class UnBanCommand extends Command {
    public UnBanCommand() {
        super("unban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission(Main.config.getString("Permissions." + getName()))) {
            //unban <Player>
            if(args.length == 1) {
                UUID uuid = UUIDUtil.getOfflinePlayer(args[0]);
                if(uuid != null) {
                    try {
                        Main.guardianDB.getTable("uuid-ban").deleteAllWhereIs("uuid", uuid.toString());
                        Main.guardianDB.getTable("ip-ban").deleteAllWhereIs("uuid", uuid.toString());
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§aSuccessfully un banned §6" + args[0] + "§a!"));
                        for (ProxiedPlayer p : Main.plugin.getProxy().getPlayers())
                            if (p.hasPermission(Main.config.getString("UnBan.see")))
                                p.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("UnBan.seeMessage") .replaceAll("%PLAYER%", sender.getName()).replaceAll("/n", "\n").replaceAll("%UNBANNED%", args[0])));

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
