package org.kayteam.simplefly.fly;

import com.cryptomorin.xseries.messages.ActionBar;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.kayteam.kayteamapi.scheduler.Task;
import org.kayteam.simplefly.SimpleFly;
import org.kayteam.simplefly.util.Color;

public class FlyTask extends Task {

    private final SimpleFly plugin;
    public int timeElapsed = 0;
    private final Player player;

    private BossBar bossBar;

    private int payTime = 0;

    public FlyTask(SimpleFly plugin, Player player) {
        super(plugin, 20L);
        this.plugin = plugin;
        this.player = player;
        plugin.getFlyManager().getPlayersFlying().put(player, this);
        if(plugin.config.getBoolean("info.boss-bar.enabled")){
            if(plugin.isBossBar()){
                bossBar = Bukkit.createBossBar(Color.convert(plugin.config.getString("info.boss-bar.text")
                                .replaceAll("%time%", String.valueOf(timeElapsed+1)))
                                , BarColor.valueOf(plugin.config.getString("info.boss-bar.color", "GREEN")), BarStyle.SOLID);
                bossBar.addPlayer(player);
                bossBar.setVisible(true);
            }
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
            if(plugin.isBossBar()){
                bossBar.setTitle(Color.convert(plugin.config.getString("info.boss-bar.text")
                        .replaceAll("%time%", String.valueOf(timeElapsed+1))));
            }
        }
        if(plugin.config.getBoolean("info.action-bar.enabled")){
            ActionBar.sendActionBar(player, Color.convert(plugin.config.getString("info.action-bar.text")
                    .replaceAll("%time%", String.valueOf(timeElapsed+1))));
        }
        timeElapsed++;
    }

    @Override
    public void stopActions() {
        FlyManager flyManager = plugin.getFlyManager();
        flyManager.getPlayersFlying().remove(player);
        try{
            if(plugin.config.getBoolean("info.boss-bar.enabled")){
                if(plugin.isBossBar()){
                    bossBar.setVisible(false);
                    bossBar.removePlayer(player);
                }
            }
        }catch (Exception ignored){}
        if(plugin.config.getBoolean("info.action-bar.enabled")){
            ActionBar.clearActionBar(player);
        }
        if(timeElapsed != 0 && !player.hasPermission("simplefly.bypass")){
            plugin.messages.sendMessage(player, "fly.end",
                    new String[][]{{"%time%", String.valueOf(timeElapsed)}, {"%cost%", String.valueOf(plugin.costPerSecond*payTime)}});
        }
    }
}
