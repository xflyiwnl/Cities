package me.xflyiwnl.cities.listener;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Citizen;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        Citizen citizen = Cities.getInstance().getCitizen(player);

        if (citizen == null) {
            citizen = new Citizen(player.getUniqueId());
            citizen.create(true);

            System.out.println("create");
        }

    }

}
