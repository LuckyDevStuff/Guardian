package de.mcgamer.guardian.commands;

import de.mcgamer.guardian.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.GroupedThreadFactory;

import java.util.ArrayList;
import java.util.Arrays;

public class KickCommand extends Command {
    public KickCommand() {
        super("kick");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender.hasPermission(Main.config.getString("Permissions." + getName()))) {
            if(args.length == 1) {
                try {
                    Main.plugin.getProxy().getPlayer(args[0]).disconnect(new TextComponent(Main.PREFIX + "\n" + Main.config.getString("Kick.message").replaceAll("%REASON%", "-").replaceAll("/n", "\n")));
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§aSuccessfully kicked the Player §6" + args[0] + "§c!"));
                } catch (Exception exception) {
                    sender.sendMessage(new TextComponent(Main.PREFIX + "§cThis Player isn't online!"));
                }
            } else if(args.length >= 1) {
                try {
                    ArrayList<String> reasonList = new ArrayList<>(Arrays.asList(args));
                    reasonList.remove(0);
                    StringBuilder reason = new StringBuilder();
                    for(String string : reasonList)
                        reason.append(ChatColor.translateAlternateColorCodes('&', string)).append(" ");
                    Main.plugin.getProxy().getPlayer(args[0]).disconnect(new TextComponent(Main.PREFIX + "\n" + Main.config.getString("Kick.message").replaceAll("%REASON%", reason.toString()).replaceAll("/n", "\n")));
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
