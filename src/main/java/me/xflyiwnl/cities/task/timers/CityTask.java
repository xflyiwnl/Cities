package me.xflyiwnl.cities.task.timers;

import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.task.CitiesTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CityTask extends CitiesTask {

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
