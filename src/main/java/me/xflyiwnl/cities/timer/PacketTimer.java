package me.xflyiwnl.cities.timer;

import me.xflyiwnl.cities.Cities;
import org.bukkit.scheduler.BukkitRunnable;

public class PacketTimer extends BukkitRunnable {

    public PacketTimer(long time) {
        this.runTaskTimer(Cities.getInstance(), 0, time * 20);
    }

    @Override
    public void run() {

//        for (Player player : Bukkit.getOnlinePlayers()) {
//
//            Citizen citizen = Cities.getInstance().getCitizen(player.getUniqueId());
//
//            if (citizen == null) {
//                System.out.println("NULL CITIZEN");
//                continue;
//            }
//
//            CitiesBuffer buffer = new CitiesBuffer();
//            buffer.writeString(!citizen.hasCity() ? ("Нет города") : citizen.getCity().getName());
//
//            player.sendPluginMessage(Cities.getInstance(), "cities:city", buffer.asByteArray());
//
//        }

    }
}
