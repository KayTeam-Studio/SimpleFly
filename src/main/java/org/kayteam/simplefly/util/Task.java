package org.kayteam.simplefly.util;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.kayteam.simplefly.SimpleFly;

public abstract class Task{

    private final SimpleFly plugin;
    private BukkitTask task;
    private final long ticks;

    public Task(SimpleFly plugin, long ticks){
        this.plugin = plugin;
        this.ticks = ticks;
    }

    public void startScheduler() {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        task = scheduler.runTaskTimer(plugin, this::actions, 0L, ticks);
    }

    public void stopScheduler(){
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.cancelTask(task.getTaskId());
        stopActions();
    }

    public abstract void actions();

    public void stopActions(){}
}