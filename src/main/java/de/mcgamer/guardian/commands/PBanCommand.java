package de.mcgamer.guardian.commands;

import de.luckydev.luckyms.MySQLException;
import de.mcgamer.guardian.Main;
import de.mcgamer.guardian.listener.LoginListener;
import de.mcgamer.guardian.util.UUIDUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Random;

public class PBanCommand extends Command {
    public PBanCommand() {
        super("pban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission(Main.config.getString("Permissions." + getName()))) {
            if (args.length == 2) {
                try {
                    if (sender instanceof ProxiedPlayer) if (sender.getName().equalsIgnoreCase(args[0])) {
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§cYou cant ban yourself"));
                        return;
                    }
                    String uuid = UUIDUtil.getOfflinePlayer(args[0]).toString();
                    String reason = args[1];
                    String bannerName = "Console";
                    if (sender instanceof ProxiedPlayer) bannerName = sender.getName();
                    long bannedAt = System.currentTimeMillis();
                    ArrayList<String> banIds = Main.guardianDB.getTable("ban").getContents("banid");
                    Random random = new Random();
                    int banId = random.nextInt(99999999);
                    while (banIds.contains("#" + banId)) banId = random.nextInt(99999999);
                    //UUID, banID, bannedAt, banTime, reason, banner, permanently
                    Main.guardianDB.getTable("ban").insert(uuid, "#" + banId, bannedAt + "", "0", reason, bannerName, "1");
                    try {
                        Main.plugin.getProxy().getPlayer(args[0]).disconnect(new TextComponent(Main.PREFIX + "\n" + LoginListener.getPBanMessage(bannedAt, reason, bannerName, "#" + banId)));
                    } catch (Exception ignored) {
                    }
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§aThe Player §6" + args[0] + " §awith uuid §6" + uuid + " §ahas been successfully permanently banned for Reason §6" + reason));
                } catch (NoSuchElementException | MySQLException exception) {
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player doesn't exist or is already banned!"));
                }
            } else
                sender.sendMessage(new TextComponent(Main.PREFIX + "§cPlease use §6/pban <PlayerName> <Reason>"));
        } else
            sender.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("Permissions.NoPermissionMessage")));
    }
}
