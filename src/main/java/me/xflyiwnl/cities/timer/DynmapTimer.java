package me.xflyiwnl.cities.timer;

import me.xflyiwnl.cities.Cities;
import org.bukkit.scheduler.BukkitRunnable;

public class DynmapTimer extends BukkitRunnable {

    public DynmapTimer(long time) {
        this.runTaskTimer(Cities.getInstance(), 0, time);
    }

    @Override
    public void run() {
        Cities.getInstance().getDynmapDrawer().updateCities();
    }

}
