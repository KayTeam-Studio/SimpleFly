package org.kayteam.simplefly.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.kayteam.simplefly.SimpleFly;

public class CommandManager {

    private SimpleFly plugin;

    public CommandManager(SimpleFly plugin) {
        this.plugin = plugin;
    }

    public boolean playerHasPerm(CommandSender sender, String permission){
        if(sender.hasPermission(permission)){
            return true;
        }else{
            plugin.messages.sendMessage(sender, "no-permissions");
            return false;
        }
    }
    public boolean playerHasPerm(Player player, String permission){
        if(player.hasPermission(permission)){
            return true;
        }else{
            plugin.messages.sendMessage(player, "no-permissions");
            return false;
        }
    }

    public void insufficientArgs(Player player, String usage){
        plugin.messages.sendMessage(player, "insufficient-args", new String[][]{{"%usage%", usage}});
    }

    public void insufficientArgs(CommandSender sender, String usage){
        plugin.messages.sendMessage(sender, "insufficient-args", new String[][]{{"%usage%", usage}});
    }
}
