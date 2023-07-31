package me.xflyiwnl.cities.command;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.City;
import me.xflyiwnl.cities.object.Country;
import me.xflyiwnl.cities.object.Translator;
import me.xflyiwnl.cities.object.invite.CityInvite;
import me.xflyiwnl.cities.object.invite.CountryInvite;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class CountryCommand implements TabCompleter, CommandExecutor {

    public List<String> countryTabCompletes = Arrays.asList(
            "bank",
            "rank",
            "online",
            "broadcast",
            "kick",
            "invite",
            "add",
            "remove",
            "leave",
            "new",
            "create"
    );

    private List<String> bankTabCompletes = Arrays.asList(
            "deposit",
            "withdraw"
    );

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) {
            return countryTabCompletes;
        }

        else if (args.length == 2) {
            switch (args[1].toLowerCase()) {
                case "bank":
                    return bankTabCompletes;
                default:
                    return null;
            }
        }

        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        Citizen citizen = Cities.getInstance().getCitizen(player);

        if (citizen == null) {
            return true;
        }

        parseCountryCommand(citizen, args);

        return true;
    }

    public void parseCountryCommand(Citizen citizen, String[] args) {

        if (args.length == 0) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        switch (args[0].toLowerCase()) {
            case "bank":
                bankCommand(citizen, args);
                break;
            case "rank":
                break;
            case "online":
                break;
            case "broadcast":
                broadcastCommand(citizen, args);
                break;
            case "kick":
                kickCommand(citizen, args);
                break;
            case "invite":
                inviteCommand(citizen, args);
                break;
            case "add":
                inviteCommand(citizen, args);
                break;
            case "remove":
                removeCommand(citizen, args);
                break;
            case "leave":
                leaveCommand(citizen, args);
                break;
            case "new":
                createCommand(citizen, args);
                break;
            case "create":
                createCommand(citizen, args);
                break;
            case "set":
                // todo
                break;
            default:
                Translator.send(citizen)
                        .path("command.unknown-arg")
                        .run();
                break;
        }

    }

    public void leaveCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCountry()) {
            Translator.send(citizen)
                    .path("citizen.no-country")
                    .run();
            return;
        }

        Country country = citizen.getCountry();

        if (citizen.isMayor()) {
            Translator.send(citizen)
                    .path("city.not-mayor")
                    .run();
            return;
        }

        City city = citizen.getCity();

        // todo leave
        city.setCountry(null);

        country.save();
        city.save();

    }

    public void kickCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCountry()) {
            Translator.send(citizen)
                    .path("citizen.no-country")
                    .run();
            return;
        }

        Country country = citizen.getCountry();

        if (args.length < 2) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        City receiver = Cities.getInstance().getCity(args[1]);

        if (receiver == null) {
            Translator.send(citizen)
                    .path("city.unknown-city")
                    .run();
            return;
        }

        if (!country.getCities().contains(receiver)) {
            Translator.send(citizen)
                    .path("country.not-contains-city")
                    .run();
            return;
        }

        if (!receiver.isCapital()) {
            Translator.send(citizen)
                    .path("country.kick-capital")
                    .run();
            return;
        }

        // kick todo
        country.save();

    }

    public void inviteCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCountry()) {
            Translator.send(citizen)
                    .path("citizen.no-country")
                    .run();
            return;
        }

        Country country = citizen.getCountry();

        if (args.length < 2) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        City receiver = Cities.getInstance().getCity(args[1]);

        if (receiver == null) {
            Translator.send(citizen)
                    .path("city.unknown-city")
                    .run();
            return;
        }

        if (country.getCities().contains(receiver)) {
            Translator.send(citizen)
                    .path("country.contains-city")
                    .run();
            return;
        }

        if (receiver.hasCountry()) {
            Translator.send(citizen)
                    .path("invite.receiver-has.country")
                    .replace("country", receiver.getCountry().getName())
                    .run();
            return;
        }

        if (receiver.hasInvite()) {
            Translator.send(citizen)
                    .path("invite.receiver-has-invite")
                    .run();
            return;
        }

        CountryInvite invite = new CountryInvite(
                country,
                citizen, receiver
        );
        receiver.setInvite(invite);

    }

    public void broadcastCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCountry()) {
            Translator.send(citizen)
                    .path("citizen.no-country")
                    .run();
            return;
        }

        if (args.length < 2) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(i + 1 == args.length ? args[i] : args[i] + " ");
        }

        citizen.getCountry().broadcast(sb.toString(), true);

        Translator.send(citizen)
                .path("citizen.broadcast-send")
                .run();

    }

    public void bankCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCountry()) {
            Translator.send(citizen)
                    .path("citizen.no-country")
                    .run();
            return;
        }

        Country country = citizen.getCountry();

        if (args.length == 1) {
            // todo open lands menu
            return;
        }

        if (args.length < 3) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        double amount = Double.valueOf(args[2]);

        if (amount < 1 && amount > 1000000) {
            Translator.send(citizen)
                    .path("economy.amount-error")
                    .run();
            return;
        }

        switch (args[1].toLowerCase()) {
            case "deposit":

                if (citizen.getBank().current() < amount) {
                    Translator.send(citizen)
                            .path("economy.not-enough-money.citizen")
                            .run();
                    return;
                }

                citizen.getBank().pay(country.getBank(), amount);

                country.broadcast(Translator.of("economy.deposit-format")
                        .replace("%name%", country.getName())
                        .replace("%player%", citizen.getName())
                        .replace("%amount%", String.valueOf(amount)), false);

                break;
            case "withdraw":

                if (country.getBank().current() < amount) {
                    Translator.send(citizen)
                            .path("economy.not-enough-money.country")
                            .run();
                    return;
                }

                country.getBank().pay(citizen.getBank(), amount);

                country.broadcast(Translator.of("economy.withdraw-format")
                        .replace("%name%", country.getName())
                        .replace("%player%", citizen.getName())
                        .replace("%amount%", String.valueOf(amount)), false);

                break;
            default:
                Translator.send(citizen)
                        .path("command.unknown-arg")
                        .run();
                break;
        }

        country.save();

    }

    public void removeCommand(Citizen citizen, String[] args) {

        Country country = citizen.getCountry();

        if (country == null) {
            Translator.send(citizen)
                    .path("citizen.no-country")
                    .run();
            return;
        }

        if (!citizen.isKing()) {
            Translator.send(citizen)
                    .path("citizen.not-king")
                    .run();
            return;
        }

        Translator.send(citizen)
                .replace("country", country.getName())
                .path("country.country-removed")
                .run();

        country.remove();

    }

    public void createCommand(Citizen citizen, String[] args) {

        City city = citizen.getCity();

        if (city == null) {
            Translator.send(citizen)
                    .path("citizen.no-city")
                    .run();
            return;
        }

        if (citizen.getCountry() == null) {
            Translator.send(citizen)
                    .path("citizen.no-country")
                    .run();
            return;
        }

        if (!citizen.isKing()) {
            Translator.send(citizen)
                    .path("citizen.not-king")
                    .run();
            return;
        }

        if (args.length < 2) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        Country country = Cities.getInstance().getCountry(args[1]);

        if (country != null) {
            Translator.send(citizen)
                    .path("country.creation-name-error")
                    .run();
            return;
        }

        country = new Country(args[1], citizen, city);
        country.create(true);

        Translator.send(citizen)
                .path("country.country-created")
                .replace("country", country.getName())
                .run();

    }

}
