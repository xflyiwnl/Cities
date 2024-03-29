package me.xflyiwnl.cities.util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class TextUtil {

    public static String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg
                .replace("{main}", "#FCD05C")
                .replace("{second}", "#F1B823")
                .replace("{default}", "#FFF4D6")
                .replaceAll("#([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])([a-fA-F0-9])", "&x&$1&$2&$3&$4&$5&$6"));
    }

    public static List<String> colorize(List<String> list) {
        List<String> hexList = new ArrayList<String>();

        for (String key : list) {
            hexList.add(colorize(key));
        }

        return hexList;
    }

}
