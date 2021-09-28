package org.kayteam.simplefly.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.kayteam.simplefly.SimpleFly;
import org.kayteam.simplefly.fly.FlyTask;

public class JoinEvent implements Listener {

    private final SimpleFly plugin;

    public JoinEvent(SimpleFly plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event){
        plugin.getFlyManager().giveFreeTime(event.getPlayer(), 0);
        if(event.getPlayer().getGameMode() == GameMode.ADVENTURE || event.getPlayer().getGameMode() == GameMode.SURVIVAL){
            if(event.getPlayer().isFlying()){
                FlyTask flyTask = new FlyTask(plugin, event.getPlayer());
                flyTask.startScheduler();
                plugin.getFlyManager().getPlayersFlying().put(event.getPlayer(), flyTask);
            }
        }
    }
}
