package me.xflyiwnl.cities.timer;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.ask.Ask;
import org.bukkit.scheduler.BukkitRunnable;

public class AskTimer extends BukkitRunnable {

    private Ask ask;

    public AskTimer(Ask ask, long seconds) {
        this.ask = ask;
        this.runTaskLater(Cities.getInstance(), seconds * 20);
    }

    @Override
    public void run() {
        ask.timeOut();
    }

}
