package de.luckydev.guardian.commands;

import de.luckydev.guardian.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.Arrays;

public class KickCommand extends Command {
    public KickCommand() {
        super("kick");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission(Main.config.getString("Permissions." + getName()))) {
            if(args.length >= 1) {
                try {
                    ArrayList<String> reasonList = new ArrayList<>(Arrays.asList(args));
                    reasonList.remove(0);
                    StringBuilder reason = new StringBuilder();
                    for(String string : reasonList)
                        reason.append(ChatColor.translateAlternateColorCodes('&', string)).append(" ");
                    ProxiedPlayer player = Main.plugin.getProxy().getPlayer(args[0]);
                    player.disconnect(new TextComponent(Main.config.getString("Kick.message").replaceAll("%REASON%", ChatColor.translateAlternateColorCodes('&', reason.toString())).replaceAll("/n", "\n").replaceAll("%KICKEDBY%", sender instanceof ProxiedPlayer?sender.getName():"Console")));
                    for(ProxiedPlayer p : Main.plugin.getProxy().getPlayers())
                        if(p.hasPermission(Main.config.getString("Kick.see")))
                            p.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("Kick.seeMessage").replaceAll("%REASON%", ChatColor.translateAlternateColorCodes('&', reason.toString())).replaceAll("%PLAYER%", sender instanceof ProxiedPlayer?sender.getName():"Console").replaceAll("/n", "\n").replaceAll("%KICKED%", player.getName())));
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§aSuccessfully kicked the Player §6" + args[0] + "§c!"));
                } catch (Exception exception) {
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player isn't online!"));
                }
            } else
                sender.sendMessage(new TextComponent(Main.PREFIX + "§cPlease use §6/kick <Player> [Message]"));
        } else
            sender.sendMessage(new TextComponent(Main.PREFIX + Main.config.getString("Permissions.NoPermissionMessage")));
    }
}
