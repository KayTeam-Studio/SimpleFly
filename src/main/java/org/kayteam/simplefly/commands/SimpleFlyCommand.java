package org.kayteam.simplefly.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kayteam.simplefly.SimpleFly;
import org.kayteam.simplefly.util.CommandManager;

public class SimpleFlyCommand implements CommandExecutor {

    private final SimpleFly plugin;

    public SimpleFlyCommand(SimpleFly plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        CommandManager commandManager = new CommandManager(plugin);
        if(args.length>0){
            switch (args[0]){
                case "time":{
                    if(args.length>1){
                        try{
                            switch (args[1]){
                                case "info":{
                                    if(args.length>2){
                                        if(commandManager.playerHasPerm(sender, "simplefly.time.info.others")){
                                            Player target = Bukkit.getPlayer(args[2]);
                                            if(target != null) {
                                                plugin.messages.sendMessage(sender, "fly.time-info",
                                                        new String[][]{{"%time%", String.valueOf(plugin.getFlyManager().getPlayersData().get(target))},
                                                                {"%player%", target.getName()}});
                                            }else{
                                                plugin.messages.sendMessage(sender, "invalid-player");
                                            }
                                        }
                                    }else{
                                        if(commandManager.playerHasPerm(sender, "simplefly.time.info")){
                                            plugin.messages.sendMessage(sender, "fly.time-info",
                                                    new String[][]{{"%time%", String.valueOf(plugin.getFlyManager().getPlayersData().get((Player)sender))}});
                                        }
                                    }
                                    break;
                                }
                                case "set":{
                                    if(commandManager.playerHasPerm(sender, "simplefly.time.set")){
                                        if(args.length>3){
                                            Player target = Bukkit.getPlayer(args[2]);
                                            if(target != null) {
                                                int time = Integer.parseInt(args[3]);
                                                plugin.getFlyManager().setFreeTime(target, time);
                                                plugin.messages.sendMessage(sender, "admin.time-setted",
                                                        new String[][]{{"%time%", String.valueOf(time)},
                                                        {"%player%", target.getName()}});
                                            }else{
                                                plugin.messages.sendMessage(sender, "invalid-player");
                                            }
                                        }else{
                                            commandManager.insufficientArgs(sender, "/sf time set <player> <seconds>");
                                        }
                                    }
                                    break;
                                }
                                case "give":{
                                    if(commandManager.playerHasPerm(sender, "simplefly.time.give")){
                                        if(args.length>3){
                                            Player target = Bukkit.getPlayer(args[2]);
                                            if(target != null) {
                                                int time = Integer.parseInt(args[3]);
                                                plugin.getFlyManager().giveFreeTime(target, time);
                                                plugin.messages.sendMessage(sender, "admin.time-given",
                                                        new String[][]{{"%time%", String.valueOf(time)},
                                                        {"%player%", target.getName()}});
                                            }else{
                                                plugin.messages.sendMessage(sender, "invalid-player");
                                            }
                                        }else{
                                            commandManager.insufficientArgs(sender, "/sf time give <player> <seconds>");
                                        }
                                    }
                                    break;
                                }
                                case "remove":{
                                    if(commandManager.playerHasPerm(sender, "simplefly.time.remove")){
                                        if(args.length>3){
                                            Player target = Bukkit.getPlayer(args[2]);
                                            if(target != null) {
                                                int time = Integer.parseInt(args[3]);
                                                plugin.getFlyManager().removeFreeTime(target, time);
                                                plugin.messages.sendMessage(sender, "admin.time-removed",
                                                        new String[][]{{"%time%", String.valueOf(time)},
                                                                {"%player%", target.getName()}});
                                            }else{
                                                plugin.messages.sendMessage(sender, "invalid-player");
                                            }
                                        }else{
                                            commandManager.insufficientArgs(sender, "/sf time remove <player> <seconds>");
                                        }
                                    }
                                    break;
                                }
                                default:{
                                    commandManager.insufficientArgs(sender, "/sf time <set-give-remove> <player> <seconds>");
                                    break;
                                }
                            }
                        }catch (Exception e){
                            commandManager.insufficientArgs(sender, "/sf time <set-give-remove> <player> <seconds>");
                        }
                    }else{
                        if (commandManager.playerHasPerm(sender, "simplefly.help")) {
                            plugin.messages.sendMessage(sender, "help");
                        }
                    }
                    break;
                }
                case "reload": {
                    if (commandManager.playerHasPerm(sender, "simplefly.use")) {
                        plugin.messages.reloadFileConfiguration();
                        plugin.config.reloadFileConfiguration();
                        plugin.messages.sendMessage(sender, "admin.reload");
                    }
                    break;
                }
                default:{
                    if (commandManager.playerHasPerm(sender, "simplefly.help")) {
                        plugin.messages.sendMessage(sender, "help");
                    }
                    break;
                }
            }
        }else{
            if (commandManager.playerHasPerm(sender, "simplefly.help")) {
                plugin.messages.sendMessage(sender, "help");
            }
        }
        return false;
    }
}
