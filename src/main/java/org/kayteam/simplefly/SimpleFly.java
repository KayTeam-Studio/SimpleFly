package org.kayteam.simplefly;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.simplefly.commands.FlyCommand;
import org.kayteam.simplefly.commands.SimpleFlyCommand;
import org.kayteam.simplefly.fly.FlyManager;
import org.kayteam.simplefly.fly.FlyTask;
import org.kayteam.simplefly.listeners.JoinEvent;
import org.kayteam.simplefly.listeners.LeaveEvent;
import org.kayteam.simplefly.listeners.ToggleFlyEvent;
import org.kayteam.simplefly.placeholderapi.SimpleFlyExpansion;
import org.kayteam.simplefly.util.KayTeam;
import org.kayteam.simplefly.util.Metrics;
import org.kayteam.simplefly.util.UpdateChecker;
import org.kayteam.simplefly.util.Yaml;

public class SimpleFly extends JavaPlugin {

    public Yaml config = new Yaml(this, "config");
    public Yaml messages = new Yaml(this, "messages");

    private static Economy econ = null;
    private FlyManager flyManager = new FlyManager(this);

    public int costPerSecond = 0;

    @Override
    public void onEnable() {
        registerFiles();
        enableUpdateChecker();
        enableStats();
        setupEconomy();
        registerListeners();
        checkPlayersFliying();
        registerCommands();
        new SimpleFlyExpansion(this).register();
        loadPlayersData();
        KayTeam.sendBrandMessage(this, "&aEnabled");
    }

    private void loadPlayersData() {
        for(Player player : getServer().getOnlinePlayers()){
            getFlyManager().giveFreeTime(player, 0);
        }
    }

    public static Economy getEconomy() {
        return econ;
    }

    private void registerCommands() {
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("simplefly").setExecutor(new SimpleFlyCommand(this));
    }

    private void checkPlayersFliying() {
        for(Player player : getServer().getOnlinePlayers()){
            if(player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL){
                if(player.getAllowFlight()){
                    new FlyTask(this, player).startScheduler();
                }
            }
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ToggleFlyEvent(this), this);
        getServer().getPluginManager().registerEvents(new LeaveEvent(this), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
    }

    private void registerFiles(){
        config.registerFileConfiguration();
        messages.registerFileConfiguration();
        costPerSecond = config.getInt("cost-per-second");
    }

    private void enableUpdateChecker(){
        /*
        updateChecker = new UpdateChecker(this, 0);
        if (updateChecker.getUpdateCheckResult().equals(UpdateChecker.UpdateCheckResult.OUT_DATED)) {
            updateChecker.sendOutDatedMessage(getServer().getConsoleSender());
        }*/
    }

    public FlyManager getFlyManager() {
        return flyManager;
    }

    private UpdateChecker updateChecker;
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    private void enableStats(){
        int pluginId = 12374;
        Metrics metrics = new Metrics(this, pluginId);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onDisable() {
        for(FlyTask task : getFlyManager().getPlayersFlying().values()){
            task.stopScheduler();
        }
        KayTeam.sendBrandMessage(this, "&cDisabled");
    }
}