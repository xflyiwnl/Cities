package me.xflyiwnl.cities.task.timers;

import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.land.Land;
import me.xflyiwnl.cities.task.CitiesTask;
import me.xflyiwnl.cities.util.Settinger;
import me.xflyiwnl.cities.util.TextUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CityTask extends CitiesTask {

    @Override
    public void run() {
        checkPlayers();
    }

    public void checkPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendAction(player);
        }
    }

    public void sendAction(Player player) {
        Land land = CitiesAPI.getInstance().getLandByLocation(player.getLocation());

        if (land == null)
            return;

        City city = land.getCity();

        if (city == null)
            return;

        if (land.isSpawnLand()) {

        }

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(TextUtil.colorize(Settinger.ofString("action-bar.default")
                .replace("%city%", city.getName()) +
                (land.isSelling()
                        ? Settinger.ofString("action-bar.placeholders.sell")
                        .replace("%price%", String.valueOf(land.getPrice())) : "") +
                (land.getOwner() != null
                        ? Settinger.ofString("action-bar.placeholders.owner")
                        .replace("%owner%", land.getOwner().getName()) : "") +
                (land.isSpawnLand()
                        ? Settinger.ofString("action-bar.placeholders.spawn-land") : ""))));

    }

}
