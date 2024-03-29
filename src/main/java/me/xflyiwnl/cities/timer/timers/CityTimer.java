package me.xflyiwnl.cities.timer.timers;

import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.timer.CitiesTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CityTimer extends CitiesTimer {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            City city = CitiesAPI.getInstance().getCityByLocation(player.getLocation());
            if (city != null) {
                player.sendActionBar(city.getName());
            }
        }
    }

}
