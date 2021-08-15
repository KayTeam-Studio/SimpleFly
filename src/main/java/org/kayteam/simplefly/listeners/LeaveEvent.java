package org.kayteam.simplefly.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.kayteam.simplefly.SimpleFly;

public class LeaveEvent implements Listener {

    private SimpleFly plugin;

    public LeaveEvent(SimpleFly plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent event){
        if(plugin.getFlyManager().getPlayersFlying().containsKey(event.getPlayer())){
            plugin.getFlyManager().getPlayersFlying().get(event.getPlayer()).stopScheduler();
        }
    }
}
