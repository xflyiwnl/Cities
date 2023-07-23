package me.xflyiwnl.cities.listener;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Citizen;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        Citizen citizen = Cities.getInstance().getCitizen(player);

        if (citizen == null) {
            citizen = new Citizen(player.getUniqueId());
            citizen.create(true);
        }

    }

    @EventHandler
    public void onLeft(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        Citizen citizen = Cities.getInstance().getCitizen(player);

        if (citizen == null) {
            return;
        }

        if (citizen.hasConfirmation()) {
            citizen.getConfirmation().remove();
        }

        if (citizen.hasInvite()) {
            citizen.getInvite().remove();
        }

    }

}
