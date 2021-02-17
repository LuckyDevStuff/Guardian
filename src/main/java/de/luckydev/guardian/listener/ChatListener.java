package de.luckydev.guardian.listener;

import de.luckydev.guardian.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(ChatEvent event) {
        for(ProxiedPlayer player : Main.plugin.getProxy().getPlayers()) {
            if(player.getAddress().getHostName().equals(event.getSender().getAddress().getHostName())) {
                if(event.getMessage().startsWith(Main.config.getString("TeamChat.prefix")) && player.hasPermission(Main.config.getString("TeamChat.permission"))) {
                    for(ProxiedPlayer team : Main.plugin.getProxy().getPlayers())
                        if(team.hasPermission(Main.config.getString("TeamChat.permission")))
                            team.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("TeamChat.messageSchema").replaceAll("%SENDER%", player.getName()).replaceAll("%MESSAGE%", ChatColor.translateAlternateColorCodes('&', event.getMessage().replaceAll(Main.config.getString("TeamChat.prefix"), "").replaceAll(Main.config.getString("TeamChat.prefix") + " ", "")))));
                    event.setCancelled(true);
                }
            }
        }
    }
}
