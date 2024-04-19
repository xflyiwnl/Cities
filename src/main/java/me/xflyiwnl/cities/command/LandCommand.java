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
                    citizen.sendMessage("no city");
                    return;
                }

                City city = citizen.getCity();
                Land land = CitiesAPI.getInstance().getLandByLocation(citizen.getLocation());

                if (land == null || !land.hasCity() || !land.isSelling()) {
                    citizen.sendMessage("null land || no city || !is selling");
                    return;
                }

                if (land.getOwner() != null && land.getOwner().equals(citizen)) {
                    citizen.sendMessage("owner");
                    return;
                }

                if (citizen.getBank().current() < land.getPrice()) {
                    citizen.sendMessage("no money");
                    return;
                }

                land.setOwner(citizen);
                land.setPrice(0);
                land.setSelling(false);

                citizen.getBank().pay(city.getBank(), land.getPrice());

                land.save();
                citizen.sendMessage("buy");

            }
            case "sell" -> {

                if (!citizen.hasCity()) {
                    citizen.sendMessage("no city");
                    return;
                }

                Land land = CitiesAPI.getInstance().getLandByLocation(citizen.getLocation());

                if (land == null || !land.hasCity()) {
                    citizen.sendMessage("null land or land has no city");
                    return;
                }

                if (!citizen.getCity().equals(land.getCity())) {
                    citizen.sendMessage("city != citizen's city");
                    return;
                }

//                if (citizen.hasPermission(PermissionNode.CITY_LAND_SELL)) {
//                    citizen.sendMessage(Translator.of("command.no-permission"));
//                    return;
//                }

                if (land.isSelling()) {
                    land.setSelling(false);
                    land.save();
                    citizen.sendMessage("canceled selling");
                } else {

                    if (args.length < 2) {
                        citizen.sendMessage("args error");
                        return;
                    }

                    double price = 0;

                    try {
                        price = Double.parseDouble(args[1]);
                    } catch (NumberFormatException e) {
                        citizen.sendMessage("number exception");
                        return;
                    }

                    land.setSelling(true);
                    land.setPrice(price);

                    land.save();

                    citizen.sendMessage("sell");
                }


            }
            default -> {
                citizen.sendMessage(Translator.of("command.unknown-arg"));
            }
        }

    }

}
