package de.mcgamer.guardian.commands;

import de.luckydev.luckyms.MySQLException;
import de.mcgamer.guardian.Main;
import de.mcgamer.guardian.listener.LoginListener;
import de.mcgamer.guardian.util.UUIDUtil;
import net.md_5.bungee.api.ChatColor;
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
                    UUID uuid = UUIDUtil.getOfflinePlayer(args[0]);
                    ProxiedPlayer player = Main.plugin.getProxy().getPlayer(args[0]);
                    if(uuid != null) {
                        if (!Main.cantBan.contains(uuid.toString())) {
                            ArrayList<String> time = new ArrayList<>(Arrays.asList(args));
                            String reason = args[1].replaceAll("_", " ");
                            String bannedBy = sender instanceof ProxiedPlayer?sender.getName():"Console";
                            time.remove(0);
                            time.remove(0);
                            Long banTime = getMsFromStringList(time);
                            long bannedAt = System.currentTimeMillis();
                            if (banTime != null) {
                                ArrayList<String> banIds = Main.guardianDB.getTable("uuid-ban").getContents("banId");
                                Random random = new Random();
                                long banId = random.nextInt(99999999);
                                while (banIds.contains("#" + banId)) banId = random.nextInt(99999999);
                                //UUID, banID, bannedAt, banTime, reason, bannedBy
                                Main.guardianDB.getTable("uuid-ban").insert(uuid.toString(), "" + banId, bannedAt + "", banTime + "", reason, bannedBy);
                                for (ProxiedPlayer p : Main.plugin.getProxy().getPlayers())
                                    if (p.hasPermission(Main.config.getString("Ban.see")))
                                        p.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("Ban.seeMessage") .replaceAll("%REASON%", ChatColor.translateAlternateColorCodes('&', reason)) .replaceAll("%PLAYER%", bannedBy).replaceAll("/n", "\n").replaceAll("%BANNED%", args[0]).replaceAll("%TIME%", LoginListener.toTime(banTime))));
                                try {
                                    Main.guardianDB.getTable("ip-ban").insert(player.getAddress().getHostName(), "" + banId, "" + bannedAt, "" + banTime, reason, bannedBy, uuid.toString());
                                    player.disconnect(new TextComponent(LoginListener.getTBanMessage(bannedAt, banTime, reason, bannedBy, banId)));
                                } catch (Exception e) {
                                }
                                sender.sendMessage(new TextComponent(Main.PREFIX + "§aThe Player §6" + args[0] + " §awith uuid §6" + uuid + " §ahas been successfully temporary banned for Reason §6" + reason));
                            } else
                                sender.sendMessage(new TextComponent(Main.PREFIX + "§cPlease Check your time Syntax!"));
                        } else
                            sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player cant be banned!"));
                    } else
                        sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player doesn't exist!"));
                } catch (MySQLException exception) {
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player is already banned!"));
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
                if (string.endsWith("y")) {
                    double ti = Float.parseFloat(string.replaceAll("y", ""));
                    time += Math.round(ti * 356 * 24 * 60 * 60 * 1000);
                } else if (string.endsWith("mo")) {
                    double ti = Float.parseFloat(string.replaceAll("mo", ""));
                    time += Math.round(ti * 30 * 24 * 60 * 60 * 1000);
                } else if (string.endsWith("w")) {
                    double ti = Float.parseFloat(string.replaceAll("w", ""));
                    time += Math.round(ti * 7 * 24 * 60 * 60 * 1000);
                } else if (string.endsWith("d")) {
                    double ti = Float.parseFloat(string.replaceAll("d", ""));
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
