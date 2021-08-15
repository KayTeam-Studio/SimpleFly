package org.kayteam.simplefly.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.kayteam.simplefly.SimpleFly;
import org.kayteam.simplefly.fly.FlyManager;
import org.kayteam.simplefly.fly.FlyTask;

public class ToggleFlyEvent implements Listener {

    private SimpleFly plugin;

    public ToggleFlyEvent(SimpleFly plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onToggleFly(PlayerToggleFlightEvent event){
        FlyManager flyManager = plugin.getFlyManager();
        if(event.getPlayer().getGameMode() == GameMode.ADVENTURE || event.getPlayer().getGameMode() == GameMode.SURVIVAL){
            if(event.isFlying()){
                new FlyTask(plugin, event.getPlayer()).startScheduler();
            }else{
                flyManager.getPlayersFlying().get(event.getPlayer()).stopScheduler();
            }
        }
    }
}
