package me.xflyiwnl.cities.object.timer;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.confirmation.Confirmation;
import org.bukkit.scheduler.BukkitRunnable;

public class ConfirmationTimer extends BukkitRunnable {

    private Confirmation confirmation;

    public ConfirmationTimer(Confirmation confirmation, long time) {
        this.confirmation = confirmation;
        this.runTaskLater(Cities.getInstance(), time * 20);
    }

    @Override
    public void run() {
        confirmation.timeOut();
    }

}
