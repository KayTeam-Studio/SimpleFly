package org.kayteam.simplefly.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Color {
    public static String convert(String message){
        try{
            return ChatColor.translateAlternateColorCodes('&',message);
        }catch (Exception e){
            return message;
        }
    }

    public static List<String> convert(List<String> message){
        List<String> messageConverted = new ArrayList<>();
        for(String line : message){
            messageConverted.add(convert(line));
        }
        return messageConverted;
    }
}
