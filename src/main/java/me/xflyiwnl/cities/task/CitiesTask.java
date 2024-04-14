package me.xflyiwnl.cities.task;

import me.xflyiwnl.cities.Cities;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class CitiesTask extends BukkitRunnable {

    public void startTask(long seconds) {
        this.runTaskTimer(Cities.getInstance(), 0, seconds * 20L);
    }

    public void startLater(long seconds) {
        this.runTaskLater(Cities.getInstance(), seconds * 20L);
    }

}
