package de.mcgamer.guardian.listener;

import de.luckydev.luckyms.MySQLException;
import de.mcgamer.guardian.Main;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        if(Main.guardianDB.getTable("ban").exists("uuid", uuid)) {
            try {
                long bannedAt = Main.guardianDB.getTable("ban").lGetWhereIs("bannedat", "uuid", uuid);
                long banTime = Main.guardianDB.getTable("ban").lGetWhereIs("bantime", "uuid", uuid);
                String reason = Main.guardianDB.getTable("ban").sGetWhereIs("reason", "uuid", uuid);
                String bannerName = Main.guardianDB.getTable("ban").sGetWhereIs("bannername", "uuid", uuid);
                String banID = Main.guardianDB.getTable("ban").sGetWhereIs("banid", "uuid", uuid);

                boolean permanently = Main.guardianDB.getTable("ban").bGetWhereIs("permanently", "uuid", uuid);

                if(!permanently) {
                    if (System.currentTimeMillis() - bannedAt < banTime) {
                        event.getPlayer().disconnect(new TextComponent(Main.PREFIX + "\n" + getTBanMessage(bannedAt, banTime, reason, bannerName, banID)));
                    } else {
                        Main.guardianDB.getTable("ban").deleteAllWhereIs("uuid", uuid);
                    }
                } else {
                    event.getPlayer().disconnect(new TextComponent(Main.PREFIX + "\n" + getPBanMessage(bannedAt, reason, bannerName, banID)));
                }
            } catch (MySQLException ignored) { }
        }
    }

    public static String getPBanMessage(long bannedAt, String reason, String bannerName, String banID) {
        StringBuilder message = new StringBuilder();
        for(String string : Main.config.getStringList("Ban.PBanMessage"))
            message.append(string.replaceAll("%REASON%", reason.replaceAll("_", " ")).replaceAll("%BANNERNAME%", bannerName).replaceAll("%BANID%", banID).replaceAll("%BANNEDAT%", new SimpleDateFormat("yyyy/MM/dd H:m").format(new Date(bannedAt)))).append("\n");
        return message.toString();
    }

    public static String getTBanMessage(long bannedAt, long banTime, String reason, String bannerName, String banID) {
        long banMillisLeft = banTime - (System.currentTimeMillis() - bannedAt);
        long months = banMillisLeft / 2592000000L;
        long days = (banMillisLeft - months * 2592000000L) / 86400000L;
        long hours = (banMillisLeft - months * 2592000000L - days * 86400000L) / 3600000L;
        long minutes = (banMillisLeft - months * 2592000000L - days * 86400000L - hours * 3600000L) / 60000L;
        long seconds = (banMillisLeft - months * 2592000000L - days * 86400000L - hours * 3600000L - minutes * 60000L) / 1000L;
        StringBuilder message = new StringBuilder();
        for(String string : Main.config.getStringList("Ban.TBanMessage"))
            message.append(string.replaceAll("%REASON%", reason.replaceAll("_", " ")).replaceAll("%TIMELEFT%", months + "M " + days + "D " + hours + "H " + minutes + "m " + seconds + "s").replaceAll("%BANNERNAME%", bannerName).replaceAll("%BANID%", banID).replaceAll("%BANNEDAT%", new SimpleDateFormat("yyyy/MM/dd H:m").format(new Date(bannedAt)))).append("\n");
        return message.toString();
    }
}
