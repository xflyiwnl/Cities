package me.xflyiwnl.cities.object.tool;

import me.xflyiwnl.cities.Cities;
import org.bukkit.scheduler.BukkitRunnable;

public class ToolRunnable extends BukkitRunnable {

    private ToolAction action;

    public ToolRunnable(ToolAction action, int time) {
        this.action = action;
        this.runTaskTimer(Cities.getInstance(), 0, time * 20L);
    }

    @Override
    public void run() {
        boolean result = action.execute();
        if (result)
            cancel();
    }

}
