package me.xflyiwnl.cities.util;

import me.xflyiwnl.cities.Cities;
import org.bukkit.Bukkit;

public class StreamUtil {

    public static void runAsync(Runnable code) {
        Bukkit.getScheduler().runTaskAsynchronously(Cities.getInstance(), code);
    }

    public static void runSync(Runnable code) {
        Bukkit.getScheduler().runTask(Cities.getInstance(), code);
    }

}
