package me.xflyiwnl.cities.command;

import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.object.citizen.Citizen;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.land.Land;
import me.xflyiwnl.cities.object.rank.PermissionNode;
import me.xflyiwnl.cities.util.Translator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class LandCommand implements TabCompleter, CommandExecutor {

    private List<String> arguments = Arrays.asList(
            "buy",
            "sell"
    );

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        switch (args.length) {
            case 1 -> {
                return arguments;
            }
        }

        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        Citizen citizen = CitiesAPI.getInstance().getCitizen(player);

        parseCommand(citizen, args);

        return true;
    }

    public void parseCommand(Citizen citizen, String[] args) {

        if (args.length == 0) {
            citizen.sendMessage(Translator.of("command.not-enough-args"));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "buy" -> {

                if (!citizen.hasCity()) {
                    citizen.sendMessage(Translator.of("citizen.no-city"));
                    return;
                }

                City city = citizen.getCity();
                Land land = CitiesAPI.getInstance().getLandByLocation(citizen.getLocation());

                if (land == null || !land.hasCity() || !land.isSelling()) {
                    citizen.sendMessage(Translator.of("land.not-selling"));
                    return;
                }

                if (land.getOwner() != null && land.getOwner().equals(citizen)) {
                    citizen.sendMessage(Translator.of("land.is-owner"));
                    return;
                }

                CitiesAPI.getInstance().createConfirmation(
                        citizen,
                        Translator.of("confirmation.confirmation-messages.buy-land")
                                .replace("%money%", String.valueOf(land.getPrice())),
                        () -> {

                            if (!land.hasCity() || !land.isSelling()) {
                                citizen.sendMessage(Translator.of("land.not-selling"));
                                return;
                            }

                            if (citizen.getBank().current() < land.getPrice()) {
                                citizen.sendMessage(Translator.of("economy.not-enough-money.citizen"));
                                return;
                            }

                            citizen.getBank().pay(city.getBank(), land.getPrice());

                            land.setOwner(citizen);
                            land.setPrice(0);
                            land.setSelling(false);


                            land.save();

                            citizen.sendMessage(Translator.of("land.buy-land")
                                    .replace("%world%", land.getCord2().getWorld().getName())
                                    .replace("%x%", String.valueOf(land.getCord2().getX()))
                                    .replace("%z%", String.valueOf(land.getCord2().getZ())));

                            city.broadcast(Translator.of("land.buy-land-broadcast")
                                            .replace("%player%", citizen.getName())
                                            .replace("%world%", land.getCord2().getWorld().getName())
                                            .replace("%x%", String.valueOf(land.getCord2().getX()))
                                            .replace("%z%", String.valueOf(land.getCord2().getZ())),
                                    true);
                        },
                        () -> {});
            }
            case "sell" -> {

                if (!citizen.hasCity()) {
                    citizen.sendMessage(Translator.of("citizen.no-city"));
                    return;
                }

                City city = citizen.getCity();
                Land land = CitiesAPI.getInstance().getLandByLocation(citizen.getLocation());

                if (land == null || !land.hasCity()) {
                    citizen.sendMessage(Translator.of("land.other-land"));
                    return;
                }

                if (land.isSpawnLand()) {
                    citizen.sendMessage(Translator.of("land.is-spawn-land"));
                    return;
                }

                if (!city.equals(land.getCity())) {
                    citizen.sendMessage(Translator.of("land.other-land"));
                    return;
                }

                if (land.getOwner() == null || !land.getOwner().equals(citizen)) {
                    if (!citizen.isMayor() && !citizen.hasPermission(PermissionNode.CITY_LAND_SELL)) {
                        citizen.sendMessage(Translator.of("command.no-permission"));
                        return;
                    }
                }

                if (land.isSelling()) {
                    land.setSelling(false);
                    land.save();

                    citizen.sendMessage(Translator.of("land.sell-cancel")
                            .replace("%world%", land.getCord2().getWorld().getName())
                            .replace("%x%", String.valueOf(land.getCord2().getX()))
                            .replace("%z%", String.valueOf(land.getCord2().getZ())));

                    city.broadcast(Translator.of("land.sell-cancel-broadcast")
                                    .replace("%player%", citizen.getName())
                                    .replace("%world%", land.getCord2().getWorld().getName())
                                    .replace("%x%", String.valueOf(land.getCord2().getX()))
                                    .replace("%z%", String.valueOf(land.getCord2().getZ())),
                            true);

                } else {

                    if (args.length < 2) {
                        citizen.sendMessage(Translator.of("command.not-enough-args"));
                        return;
                    }

                    double price = 0;

                    try {
                        price = Double.parseDouble(args[1]);
                    } catch (NumberFormatException e) {
                        citizen.sendMessage(Translator.of("other.number-exception"));
                        return;
                    }

                    land.setSelling(true);
                    land.setPrice(price);

                    land.save();

                    citizen.sendMessage(Translator.of("land.sell-land")
                            .replace("%world%", land.getCord2().getWorld().getName())
                            .replace("%x%", String.valueOf(land.getCord2().getX()))
                            .replace("%z%", String.valueOf(land.getCord2().getZ())));

                    city.broadcast(Translator.of("land.sell-land-broadcast")
                            .replace("%player%", citizen.getName())
                            .replace("%world%", land.getCord2().getWorld().getName())
                            .replace("%x%", String.valueOf(land.getCord2().getX()))
                            .replace("%z%", String.valueOf(land.getCord2().getZ())),
                            true);
                }


            }
            default -> {
                citizen.sendMessage(Translator.of("command.unknown-arg"));
            }
        }

    }

}
