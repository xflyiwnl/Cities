package me.xflyiwnl.cities.timer;

import me.xflyiwnl.cities.Cities;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class CitiesTimer extends BukkitRunnable {

    public void startTimer(long seconds) {
        this.runTaskTimer(Cities.getInstance(), 0, seconds * 20L);
    }

    public void startLater(long seconds) {
        this.runTaskLater(Cities.getInstance(), seconds * 20L);
    }

}
