package de.mcgamer.guardian.listener;

import de.luckydev.luckyms.MySQLException;
import de.luckydev.luckyms.MySQLTable;
import de.mcgamer.guardian.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String uuid = player.getUniqueId().toString();
        String ip = player.getAddress().getHostName();
        MySQLTable uuidBan = Main.guardianDB.getTable("uuid-ban");
        MySQLTable ipBan = Main.guardianDB.getTable("ip-ban");

        //CHECK IF UUID IS BANNED
        if(uuidBan.exists("uuid", uuid)) {
            //CHECK IF BAN TIME IS OVER
            try {
                long bannedAt = uuidBan.lGetWhereIs("bannedAt", "uuid", uuid);
                long banTime = uuidBan.lGetWhereIs("banTime", "uuid", uuid);
                String reason = uuidBan.sGetWhereIs("reason", "uuid", uuid);
                String bannedBy = uuidBan.sGetWhereIs("bannedBy", "uuid", uuid);
                long banId = uuidBan.lGetWhereIs("banId", "uuid", uuid);
                long banLeft = banTime - (System.currentTimeMillis()-bannedAt);
                if(banTime == -1) {
                    player.disconnect(new TextComponent(getPBanMessage(bannedAt, reason, bannedBy, banId)));
                } else if (banLeft < 0) {
                    uuidBan.deleteAllWhereIs("uuid", uuid);
                } else {
                    player.disconnect(new TextComponent(getTBanMessage(bannedAt, banTime, reason, bannedBy, banId)));
                }
                if(!ipBan.exists("ip", ip) && !(banLeft > 0 && banTime != -1)) {
                    ipBan.insert(ip, banId + "", bannedAt + "", banTime + "", reason, bannedBy, uuid);
                }
            } catch (MySQLException exception) {
                exception.printStackTrace();
                player.disconnect(new TextComponent(Main.PREFIX + "\n§cMy System generated an Exception when you logged in to secure the Server i temporarly cant let you in.\n §6Im Sorry but §4this is not caused by you §6it is because the Server Admin didn't connect the Database.\n§aPlease contact the Admin"));
            }
        }

        //CHECK IF UUID IS BANNED
        if(ipBan.exists("ip", ip)) {
            //CHECK IF BAN TIME IS OVER
            try {
                long bannedAt = ipBan.lGetWhereIs("bannedAt", "ip", ip);
                long banTime = ipBan.lGetWhereIs("banTime", "ip", ip);
                String reason = ipBan.sGetWhereIs("reason", "ip", ip);
                String bannedBy = ipBan.sGetWhereIs("bannedBy", "ip", ip);
                long banId = ipBan.lGetWhereIs("banId", "ip", ip);
                long banLeft = banTime - (System.currentTimeMillis()-bannedAt);
                if(banTime == -1) {
                    player.disconnect(new TextComponent(Main.PREFIX + "§7 IP-BAN\n" + getPBanMessage(bannedAt, reason, bannedBy, banId)));
                } else if (banLeft < 0) {
                    uuidBan.deleteAllWhereIs("uuid", uuid);
                } else {
                    player.disconnect(new TextComponent(Main.PREFIX + "§7 IP-BAN\n" + getTBanMessage(bannedAt, banTime, reason, bannedBy, banId)));
                }
                if(!uuidBan.exists("uuid", uuid) && !(banLeft > 0 && banTime != -1)) {
                    uuidBan.insert(uuid, banId + "", bannedAt + "", banTime + "", reason, bannedBy);
                }
            } catch (MySQLException exception) {
                exception.printStackTrace();
                player.disconnect(new TextComponent(Main.PREFIX + "\n§cMy System generated an Exception when you logged in to secure the Server i temporarly cant let you in.\n §6Im Sorry but §4this is not caused by you §6it is because the Server Admin didn't connect the Database.\n§aPlease contact the Admin"));
            }
        }
    }

    public static String getPBanMessage(long bannedAt, String reason, String bannedBy, long banId) {
        String message = Main.config.getString("Ban.PBan");
        message = message.replaceAll("/n", "\n").replaceAll("%BANNEDBY%", bannedBy).replaceAll("%BANID%", "" + banId).replaceAll("%REASON%", ChatColor.translateAlternateColorCodes('&', reason)).replaceAll("%BANNEDAT%", new SimpleDateFormat().format(new Date(bannedAt)));
        return message;
    }

    public static String getTBanMessage(long bannedAt, long banTime, String reason, String bannedBy, long banId) {
        long banLeft = banTime - (System.currentTimeMillis()-bannedAt);
        String message = Main.config.getString("Ban.TBan");
        message = message.replaceAll("/n", "\n").replaceAll("%BANNEDBY%", bannedBy).replaceAll("%BANID%", "" + banId).replaceAll("%REASON%", ChatColor.translateAlternateColorCodes('&', reason)).replaceAll("%BANNEDAT%", new SimpleDateFormat().format(new Date(bannedAt))).replaceAll("%TIMELEFT%", toTime(banLeft));
        return message;
    }

    public static String toTime(long banTime) {
        long months = banTime / 2592000000L;
        long days = (banTime - months * 2592000000L) / 86400000L;
        long hours = (banTime - months * 2592000000L - days * 86400000L) / 3600000L;
        long minutes = (banTime - months * 2592000000L - days * 86400000L - hours * 3600000L) / 60000L;
        long seconds = (banTime - months * 2592000000L - days * 86400000L - hours * 3600000L - minutes * 60000L) / 1000L;
        return months + "M " + days + "D " + hours + "H " + minutes + "m " + seconds + "s";
    }
}
