package me.xflyiwnl.cities.task.timers;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.buffer.CitiesBuffer;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.task.CitiesTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketTask extends CitiesTask {

    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            Citizen citizen = CitiesAPI.getInstance().getCitizen(player.getUniqueId());

            if (citizen == null) {
                System.out.println("NULL CITIZEN");
                continue;
            }

            CitiesBuffer buffer = new CitiesBuffer();
            buffer.writeString(!citizen.hasCity() ? ("Нет города") : citizen.getCity().getName());

            player.sendPluginMessage(Cities.getInstance(), Cities.getInstance().getChannelName(), buffer.asByteArray());

        }

    }
}
