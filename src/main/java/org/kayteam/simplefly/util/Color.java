package org.kayteam.simplefly.util;

import org.bukkit.ChatColor;

public class Color {

    public static String convert(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
