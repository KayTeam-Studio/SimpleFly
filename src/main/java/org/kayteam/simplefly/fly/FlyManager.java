package org.kayteam.simplefly.fly;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.kayteam.api.yaml.Yaml;
import org.kayteam.simplefly.SimpleFly;

import java.util.HashMap;
import java.util.Map;

public class FlyManager {

    private final SimpleFly PLUGIN;

    private final Map<Player, FlyTask> playersFlying = new HashMap<>();
    private final Map<Player, Integer> playersData = new HashMap<>();

    public FlyManager(SimpleFly plugin) {
        this.PLUGIN = plugin;
    }

    public Map<Player, FlyTask> getPlayersFlying() {
        return playersFlying;
    }

    public Map<Player, Integer> getPlayersData() {
        return playersData;
    }

    public void giveFreeTime(Player player, int time){
        Yaml playerData = new Yaml(PLUGIN, "data", player.getName());
        playerData.registerFileConfiguration();
        playerData.set("free-fly-time", playerData.getInt("free-fly-time")+time);
        getPlayersData().put(player, playerData.getInt("free-fly-time"));
        playerData.saveFileConfiguration();
    }

    public void setFreeTime(Player player, int time){
        Yaml playerData = new Yaml(PLUGIN, "data", player.getName());
        playerData.registerFileConfiguration();
        playerData.set("free-fly-time", time);
        getPlayersData().put(player, time);
        playerData.saveFileConfiguration();
    }

    public void removeFreeTime(Player player, int time){
        Yaml playerData = new Yaml(PLUGIN, "data", player.getName());
        playerData.registerFileConfiguration();
        playerData.set("free-fly-time", playerData.getInt("free-fly-time")-time);
        getPlayersData().put(player, playerData.getInt("free-fly-time"));
        playerData.saveFileConfiguration();
    }

    public void loadPlayersData() {
        for(Player player : PLUGIN.getServer().getOnlinePlayers()){
            giveFreeTime(player, 0);
        }
    }

    public void checkPlayersFliying() {
        for(Player player : PLUGIN.getServer().getOnlinePlayers()){
            if(player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL){
                if(player.getAllowFlight()){
                    new FlyTask(PLUGIN, player).startScheduler();
                }
            }
        }
    }
}
