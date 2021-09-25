package org.kayteam.simplefly.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.kayteam.simplefly.SimpleFly;

public class SimpleFlyExpansion extends PlaceholderExpansion {

    private final SimpleFly plugin;

    public SimpleFlyExpansion(SimpleFly plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getDescription().getName();
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if(params.equals("flying_time")){
            int time = 0;
            if(plugin.getFlyManager().getPlayersFlying().containsKey(player.getPlayer())){
                time = plugin.getFlyManager().getPlayersFlying().get(player.getPlayer()).timeElapsed;
            }
            return String.valueOf(time);
        }else if(params.equals("free_time")){
            return String.valueOf(plugin.getFlyManager().getPlayersData().get(player));
        }
        return "";
    }
}
