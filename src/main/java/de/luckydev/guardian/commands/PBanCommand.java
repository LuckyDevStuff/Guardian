package de.luckydev.guardian.commands;

import de.luckydev.luckyms.MySQLException;
import de.luckydev.guardian.Main;
import de.luckydev.guardian.listener.LoginListener;
import de.luckydev.guardian.util.UUIDUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

public class PBanCommand extends Command {
    public PBanCommand() {
        super("pban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission(Main.config.getString("Permissions.Commands." + getName()))) {
            if (args.length == 2) {
                try {
                    if (sender instanceof ProxiedPlayer) if (sender.getName().equalsIgnoreCase(args[0])) {
                            sender.sendMessage(new TextComponent(Main.PREFIX + "§cYou cant ban yourself"));
                            return;
                        }
                    UUID uuid = UUIDUtil.getOfflinePlayer(args[0]);
                    ProxiedPlayer player = Main.plugin.getProxy().getPlayer(args[0]);
                    if(uuid != null) {
                        if (!Main.cantBan.contains(uuid.toString())) {
                            String bannedBy = sender instanceof ProxiedPlayer?sender.getName():"Console";
                            String reason = args[1].replaceAll("_", " ");
                            long bannedAt = System.currentTimeMillis();
                            ArrayList<String> banIds = Main.guardianDB.getTable("uuid-ban").getContents("banId");
                            Random random = new Random();
                            int banId = random.nextInt(99999999);
                            while (banIds.contains("#" + banId)) banId = random.nextInt(99999999);
                            //UUID, banID, bannedAt, banTime, reason, bannedBy
                            Main.guardianDB.getTable("uuid-ban").insert(uuid.toString(), "" + banId, bannedAt + "", "-1", reason, bannedBy);
                            for (ProxiedPlayer p : Main.plugin.getProxy().getPlayers())
                                if (p.hasPermission(Main.config.getString("Ban.see")))
                                    p.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("Ban.seeMessage") .replaceAll("%REASON%", ChatColor.translateAlternateColorCodes('&', reason)) .replaceAll("%PLAYER%", bannedBy).replaceAll("/n", "\n").replaceAll("%BANNED%", args[0]).replaceAll("%TIME%", "§4PERMANENTLY")));
                            try {
                                Main.guardianDB.getTable("ip-ban").insert(player.getAddress().getHostName(), "" + banId, "" + bannedAt, "-1", reason, bannedBy, uuid.toString());
                                player.disconnect(new TextComponent(Main.PREFIX + "\n" + LoginListener.getPBanMessage(bannedAt, reason, bannedBy, banId)));
                            } catch (Exception ignored) {
                            }
                            sender.sendMessage(new TextComponent(Main.PREFIX + "§aThe Player §6" + args[0] + " §awith uuid §6" + uuid + " §ahas been successfully permanently banned for Reason §6" + reason));
                        } else
                            sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player cant be banned!"));
                    } else
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player doesn't exist!"));
                } catch (MySQLException exception) {
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player is already banned!"));
                }
            } else
                sender.sendMessage(new TextComponent(Main.PREFIX + "§cPlease use §6/pban <PlayerName> <Reason>"));
        } else
            sender.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("Permissions.NoPermissionMessage")));
    }
}
