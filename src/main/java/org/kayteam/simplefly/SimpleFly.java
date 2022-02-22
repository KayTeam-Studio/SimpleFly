package org.kayteam.simplefly;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.kayteam.api.BrandSender;
import org.kayteam.api.bStats.Metrics;
import org.kayteam.api.updatechecker.UpdateChecker;
import org.kayteam.api.yaml.Yaml;
import org.kayteam.simplefly.commands.FlyCommand;
import org.kayteam.simplefly.commands.SimpleFlyCommand;
import org.kayteam.simplefly.fly.FlyManager;
import org.kayteam.simplefly.fly.FlyTask;
import org.kayteam.simplefly.listeners.JoinEvent;
import org.kayteam.simplefly.listeners.LeaveEvent;
import org.kayteam.simplefly.listeners.ToggleFlyEvent;
import org.kayteam.simplefly.placeholderapi.SimpleFlyExpansion;

import java.util.Objects;

public class SimpleFly extends JavaPlugin {

    public final Yaml config = new Yaml(this, "config");
    public final Yaml messages = new Yaml(this, "messages");

    private static Economy econ = null;
    private final FlyManager flyManager = new FlyManager(this);

    public int costPerSecond = 0;

    private boolean bossBar;

    @Override
    public void onEnable() {
        registerFiles();
        enableUpdateChecker();
        enableStats();
        setupEconomy();
        registerListeners();
        flyManager.checkPlayersFliying();
        registerCommands();
        new SimpleFlyExpansion(this).register();
        flyManager.loadPlayersData();
        try {
            checkBossBar();
        } catch (ClassNotFoundException ignored) {}
        BrandSender.sendBrandMessage(this, "&aEnabled");
    }

    public boolean isBossBar() {
        return bossBar;
    }

    private void checkBossBar() throws ClassNotFoundException {
        try{
            Class.forName("org.bukkit.boss.BarColor");
            this.bossBar = true;
        }catch(NoClassDefFoundError | ClassNotFoundException e){
            this.bossBar = false;
        }
    }

    public static Economy getEconomy() {
        return econ;
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand(this));
        Objects.requireNonNull(getCommand("simplefly")).setExecutor(new SimpleFlyCommand(this));
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
        updateChecker = new UpdateChecker(this, 95365);
        if (updateChecker.getUpdateCheckResult().equals(UpdateChecker.UpdateCheckResult.OUT_DATED)) {
            updateChecker.sendOutDatedMessage(getServer().getConsoleSender());
        }
    }

    public FlyManager getFlyManager() {
        return flyManager;
    }

    private UpdateChecker updateChecker;
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    private void enableStats(){
        int pluginId = 12464;
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
        BrandSender.sendBrandMessage(this, "&cDisabled");
    }
}
