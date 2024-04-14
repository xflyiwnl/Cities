package me.xflyiwnl.cities.task.timers;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.task.CitiesTask;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeTask extends CitiesTask {

    @Override
    public void run() {

        Cities instance = Cities.getInstance();

        LocalDateTime lastDay = instance.getLastDay();
        int[] dayTime = instance.dayTime();

        LocalDateTime nextDay = lastDay.plusHours(dayTime[0])
                .plusMinutes(dayTime[1]);

        Duration between = Duration.between(LocalDateTime.now(), nextDay);

        if (!between.isNegative()) {
            return;
        }

        instance.newDay();

    }

}
