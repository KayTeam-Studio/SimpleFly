package org.kayteam.simplefly.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kayteam.simplefly.SimpleFly;
import org.kayteam.simplefly.util.CommandManager;
import org.kayteam.simplefly.util.Yaml;

public class FlyCommand implements CommandExecutor {

    private SimpleFly plugin;

    public FlyCommand(SimpleFly plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        CommandManager commandManager = new CommandManager(plugin);
        Yaml messages = plugin.messages;
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(args.length == 0){
                if(commandManager.playerHasPerm(player, "simplefly.use")){
                    if(player.getAllowFlight()){
                        player.setAllowFlight(false);
                        messages.sendMessage(player,"fly.disabled");
                    }else{
                        player.setAllowFlight(true);
                        messages.sendMessage(player,"fly.enabled");
                    }
                }
            }else{
                if(commandManager.playerHasPerm(player, "simplefly.use.other")){
                    Player target = Bukkit.getPlayer(args[0]);
                    if(target != null){
                        if(target.getAllowFlight()){
                            target.setAllowFlight(false);
                            messages.sendMessage(target,"fly.disabled");
                            messages.sendMessage(player,"fly.other.disabled", new String[][]{{"%player%", target.getName()}});
                        }else{
                            target.setAllowFlight(true);
                            messages.sendMessage(target,"fly.enabled");
                            messages.sendMessage(player,"fly.other.enabled", new String[][]{{"%player%", target.getName()}});
                        }
                    }else{
                       messages.sendMessage(player, "invalid-player");
                    }
                }
            }
        }else{
            if(args.length>0){
                Player target = Bukkit.getPlayer(args[0]);
                if(target != null){
                    if(target.getAllowFlight()){
                        target.setAllowFlight(false);
                        messages.sendMessage(target,"fly.disabled");
                        messages.sendMessage(sender,"fly.other.disabled", new String[][]{{"%player%", target.getName()}});
                    }else{
                        target.setAllowFlight(true);
                        messages.sendMessage(target,"fly.enabled");
                        messages.sendMessage(sender,"fly.other.enabled", new String[][]{{"%player%", target.getName()}});
                    }
                }else{
                   messages.sendMessage(sender, "invalid-player");
                }
            }else{
                commandManager.insufficientArgs(sender, "fly <player>");
            }
        }
        return true;
    }
}
