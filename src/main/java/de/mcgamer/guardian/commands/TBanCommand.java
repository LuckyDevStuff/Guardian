package de.mcgamer.guardian.commands;

import de.luckydev.luckyms.MySQLException;
import de.mcgamer.guardian.Main;
import de.mcgamer.guardian.listener.LoginListener;
import de.mcgamer.guardian.util.UUIDUtil;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;


public class TBanCommand extends Command {


    public TBanCommand() {
        super("tban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission(Main.config.getString("Permissions." + getName()))) {
            if (args.length >= 3) {
                try {
                    if (sender instanceof ProxiedPlayer) if (sender.getName().equalsIgnoreCase(args[0])) {
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§cYou cant ban yourself"));
                        return;
                    }
                    String uuid = UUIDUtil.getOfflinePlayer(args[0]).toString();
                    ArrayList<String> time = new ArrayList<>(Arrays.asList(args));
                    String reason = args[1];
                    String bannerName = "Console";
                    if (sender instanceof ProxiedPlayer) bannerName = sender.getName();
                    time.remove(0);
                    time.remove(0);
                    Long banTime = getMsFromStringList(time);
                    long bannedAt = System.currentTimeMillis();
                    if (banTime != null) {
                        ArrayList<String> banIds = Main.guardianDB.getTable("ban").getContents("banid");
                        Random random = new Random();
                        int banId = random.nextInt(99999999);
                        while (banIds.contains("#" + banId)) banId = random.nextInt(99999999);
                        //UUID, banID, bannedAt, banTime, reason, banner
                        Main.guardianDB.getTable("ban").insert(uuid, "#" + banId, bannedAt + "", banTime + "", reason, bannerName, "0");
                        try {
                            Main.plugin.getProxy().getPlayer(args[0]).disconnect(new TextComponent(Main.PREFIX + "\n" + LoginListener.getTBanMessage(bannedAt, banTime, reason, bannerName, "#" + banId)));
                        } catch (Exception e) {
                        }
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§aThe Player §6" + args[0] + " §awith uuid §6" + uuid + " §ahas been successfully temporary banned for Reason §6" + reason));
                    } else
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§cPlease Check your time Syntax!"));
                } catch (NoSuchElementException | MySQLException exception) {
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player doesn't exist or is already banned!"));
                }
            } else
                sender.sendMessage(new TextComponent(Main.PREFIX + "§cPlease use §6/tban <PlayerName> <Reason> <Time>"));
        } else
            sender.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("Permissions.NoPermissionMessage")));
    }

    public Long getMsFromStringList(List<String> args) {
        long time = 0;
        for(String string : args) {
            try {
                if (string.endsWith("Y")) {
                    double ti = Float.parseFloat(string.replaceAll("Y", ""));
                    time += Math.round(ti * 356 * 24 * 60 * 60 * 1000);
                } else if (string.endsWith("M")) {
                    double ti = Float.parseFloat(string.replaceAll("M", ""));
                    time += Math.round(ti * 30 * 24 * 60 * 60 * 1000);
                } else if (string.endsWith("W")) {
                    double ti = Float.parseFloat(string.replaceAll("W", ""));
                    time += Math.round(ti * 7 * 24 * 60 * 60 * 1000);
                } else if (string.endsWith("D")) {
                    double ti = Float.parseFloat(string.replaceAll("D", ""));
                    time += Math.round(ti * 24 * 60 * 60 * 1000);
                } else if (string.endsWith("h")) {
                    double ti = Float.parseFloat(string.replaceAll("h", ""));
                    time += Math.round(ti * 60 * 60 * 1000);
                } else if (string.endsWith("ms")) {
                    double ti = Float.parseFloat(string.replaceAll("ms", ""));
                    time += Math.round(ti);
                }else if (string.endsWith("m")) {
                    double ti = Float.parseFloat(string.replaceAll("m", ""));
                    time += Math.round(ti * 60 * 1000);
                } else if (string.endsWith("s")) {
                    double ti = Float.parseFloat(string.replaceAll("s", ""));
                    time += Math.round(ti * 1000);
                }  else {
                    return null;
                }
            } catch(Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }
        return time;
    }
}
