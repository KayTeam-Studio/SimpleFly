package org.kayteam.simplefly.fly;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.kayteam.simplefly.SimpleFly;
import org.kayteam.simplefly.util.Color;
import org.kayteam.simplefly.util.Task;
import org.kayteam.simplefly.util.Yaml;

public class FlyTask extends Task {

    private SimpleFly plugin;
    public int timeElapsed = 0;
    private Player player;

    private BossBar bossBar;

    private int payTime = 0;

    public FlyTask(SimpleFly plugin, Player player) {
        super(plugin, 20L);
        this.plugin = plugin;
        this.player = player;
        plugin.getFlyManager().getPlayersFlying().put(player, this);
        if(plugin.config.getBoolean("info.boss-bar.enabled")){
            bossBar = Bukkit.createBossBar(Color.convert(plugin.config.getString("info.boss-bar.text")
                            .replaceAll("%time%", String.valueOf(timeElapsed+1)))
                            , BarColor.valueOf(plugin.config.getString("info.boss-bar.color", "GREEN")), BarStyle.SOLID);
            bossBar.addPlayer(player);
            bossBar.setVisible(true);
        }
    }

    @Override
    public void actions() {
        if(!player.hasPermission("simplefly.bypass")){
            if(plugin.getFlyManager().getPlayersData().get(player) >= 1){
                plugin.getFlyManager().removeFreeTime(player,1);
            }else if(SimpleFly.getEconomy().getBalance(player) >= plugin.costPerSecond){
                SimpleFly.getEconomy().withdrawPlayer(player, plugin.costPerSecond);
                payTime++;
            }else{
                stopScheduler();
                plugin.messages.sendMessage(player, "insufficient-money",
                        new String[][]{{"%cost%", String.valueOf(plugin.costPerSecond)}});
                player.setAllowFlight(false);
                plugin.messages.sendMessage(player,"fly.disabled");
                return;
            }
        }
        FlyManager flyManager = plugin.getFlyManager();
        if(!player.isFlying()){
            stopScheduler();
            return;
        }
        if(plugin.config.getBoolean("info.boss-bar.enabled")){
            bossBar.setTitle(Color.convert(plugin.config.getString("info.boss-bar.text")
                    .replaceAll("%time%", String.valueOf(timeElapsed+1))));
        }
        if(plugin.config.getBoolean("info.action-bar.enabled")){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(Color.convert(plugin.config.getString("info.action-bar.text")
                            .replaceAll("%time%", String.valueOf(timeElapsed+1)))));
        }
        timeElapsed++;
    }

    @Override
    public void stopActions() {
        FlyManager flyManager = plugin.getFlyManager();
        flyManager.getPlayersFlying().remove(player);
        try{
            if(plugin.config.getBoolean("info.boss-bar.enabled")){
                bossBar.setVisible(false);
                bossBar.removePlayer(player);

            }
        }catch (Exception ignored){}
        if(plugin.config.getBoolean("info.action-bar.enabled")){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new TextComponent(""));
        }
        if(timeElapsed != 0 && !player.hasPermission("simplefly.bypass")){
            plugin.messages.sendMessage(player, "fly.end",
                    new String[][]{{"%time%", String.valueOf(timeElapsed)}, {"%cost%", String.valueOf(plugin.costPerSecond*payTime)}});
        }
    }
}
