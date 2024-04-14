package me.xflyiwnl.cities.task.timers;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.task.CitiesTask;

public class DynmapTask extends CitiesTask {

    @Override
    public void run() {
        Cities.getInstance().getDynmapDrawer().updateCities();
    }

}
