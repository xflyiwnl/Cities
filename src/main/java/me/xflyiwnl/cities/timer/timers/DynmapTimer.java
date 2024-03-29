package me.xflyiwnl.cities.timer.timers;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.timer.CitiesTimer;
import org.bukkit.scheduler.BukkitRunnable;

public class DynmapTimer extends CitiesTimer {

    @Override
    public void run() {
        Cities.getInstance().getDynmapDrawer().updateCities();
    }

}
