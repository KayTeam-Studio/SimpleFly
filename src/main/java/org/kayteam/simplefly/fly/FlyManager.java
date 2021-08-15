package org.kayteam.simplefly.fly;

import org.bukkit.entity.Player;
import org.kayteam.simplefly.SimpleFly;
import org.kayteam.simplefly.util.Yaml;

import java.util.HashMap;
import java.util.Map;

public class FlyManager {

    private SimpleFly plugin;

    private Map<Player, FlyTask> playersFlying = new HashMap<>();
    private Map<Player, Integer> playersData = new HashMap<>();

    public FlyManager(SimpleFly plugin) {
        this.plugin = plugin;
    }

    public Map<Player, FlyTask> getPlayersFlying() {
        return playersFlying;
    }

    public Map<Player, Integer> getPlayersData() {
        return playersData;
    }

    public void giveFreeTime(Player player, int time){
        Yaml playerData = new Yaml(plugin, "data", player.getName());
        playerData.registerFileConfiguration();
        playerData.set("free-fly-time", playerData.getInt("free-fly-time")+time);
        getPlayersData().put(player, playerData.getInt("free-fly-time"));
        playerData.saveFileConfiguration();
    }

    public void setFreeTime(Player player, int time){
        Yaml playerData = new Yaml(plugin, "data", player.getName());
        playerData.registerFileConfiguration();
        playerData.set("free-fly-time", time);
        getPlayersData().put(player, time);
        playerData.saveFileConfiguration();
    }

    public void removeFreeTime(Player player, int time){
        Yaml playerData = new Yaml(plugin, "data", player.getName());
        playerData.registerFileConfiguration();
        playerData.set("free-fly-time", playerData.getInt("free-fly-time")-time);
        getPlayersData().put(player, playerData.getInt("free-fly-time"));
        playerData.saveFileConfiguration();
    }
}
