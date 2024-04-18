package me.xflyiwnl.cities.task.timers;

import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.land.Land;
import me.xflyiwnl.cities.task.CitiesTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CityTask extends CitiesTask {

    @Override
    public void run() {
        checkPlayers();
    }

    public void checkPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendAction(player);
        }
    }

    public void sendAction(Player player) {
        Land land = CitiesAPI.getInstance().getLandByLocation(player.getLocation());
        City city = land.getCity();
        if (city == null) {
            return;
        }

        player.sendActionBar(city.getName() + " / " + (
                land.isSelling()
                ? "Продаётся * " + land.getPrice()
                : ""));

    }

}
