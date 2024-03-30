package me.xflyiwnl.cities.command;

import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Translator;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.invite.types.CountryInvite;
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
        Citizen citizen = CitiesAPI.getInstance().getCitizen(player);

        if (citizen == null) {
            return true;
        }

        parseCountryCommand(citizen, args);

        return true;
    }

    public void parseCountryCommand(Citizen citizen, String[] args) {

        if (args.length == 0) {
            citizen.sendMessage(Translator.of("command.not-enough-args"));
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
                citizen.sendMessage(Translator.of("command.unknown-arg"));
                break;
        }

    }

    public void leaveCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCountry()) {
            citizen.sendMessage(Translator.of("citizen.no-country"));
            return;
        }

        Country country = citizen.getCountry();

        if (citizen.isMayor()) {
            citizen.sendMessage(Translator.of("city.not-mayor"));
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
            citizen.sendMessage(Translator.of("citizen.no-country"));
            return;
        }

        Country country = citizen.getCountry();

        if (args.length < 2) {
            citizen.sendMessage(Translator.of("command.not-enough-args"));
            return;
        }

        City receiver = CitiesAPI.getInstance().getCity(args[1]);

        if (receiver == null) {
            citizen.sendMessage(Translator.of("city.unknown-city"));
            return;
        }

        if (!country.hasCity(receiver)) {
            citizen.sendMessage(Translator.of("country.not-contains-city"));
            return;
        }

        if (!receiver.isCapital()) {
            citizen.sendMessage(Translator.of("country.kick-capital"));
            return;
        }

        // kick todo
        country.save();

    }

    public void inviteCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCountry()) {
            citizen.sendMessage(Translator.of("citizen.no-country"));
            return;
        }

        Country country = citizen.getCountry();

        if (args.length < 2) {
            citizen.sendMessage(Translator.of("command.not-enough-args"));
            return;
        }

        City receiver = CitiesAPI.getInstance().getCity(args[1]);

        if (receiver == null) {
            citizen.sendMessage(Translator.of("city.unknown-city"));
            return;
        }

        if (country.hasCity(receiver)) {
            citizen.sendMessage(Translator.of("country.contains-city"));
            return;
        }

        if (receiver.hasCountry()) {
            citizen.sendMessage(Translator.of("invite.receiver-has.country").replace("country", receiver.getCountry().getName()));
            return;
        }

        if (receiver.hasInvite()) {
            citizen.sendMessage(Translator.of("invite.receiver-has-invite"));
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
            citizen.sendMessage(Translator.of("citizen.no-country"));
            return;
        }

        if (args.length < 2) {
            citizen.sendMessage(Translator.of("command.not-enough-args"));
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(i + 1 == args.length ? args[i] : args[i] + " ");
        }

        citizen.getCountry().broadcast(sb.toString(), true);

        citizen.sendMessage(Translator.of("citizen.broadcast-send"));

    }

    public void bankCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCountry()) {
            citizen.sendMessage(Translator.of("citizen.no-country"));
            return;
        }

        Country country = citizen.getCountry();

        if (args.length == 1) {
            // todo open lands menu
            return;
        }

        if (args.length < 3) {
            citizen.sendMessage(Translator.of("command.not-enough-args"));
            return;
        }

        double amount = Double.valueOf(args[2]);

        if (amount < 1 && amount > 1000000) {
            citizen.sendMessage(Translator.of("economy.amount-error"));
            return;
        }

        switch (args[1].toLowerCase()) {
            case "deposit":

                if (citizen.getBank().current() < amount) {
                    citizen.sendMessage(Translator.of("economy.not-enough-money.citizen"));
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
                    citizen.sendMessage(Translator.of("economy.not-enough-money.country"));
                    return;
                }

                country.getBank().pay(citizen.getBank(), amount);

                country.broadcast(Translator.of("economy.withdraw-format")
                        .replace("%name%", country.getName())
                        .replace("%player%", citizen.getName())
                        .replace("%amount%", String.valueOf(amount)), false);

                break;
            default:
                citizen.sendMessage(Translator.of("command.unknown-arg"));
                break;
        }

        country.save();

    }

    public void removeCommand(Citizen citizen, String[] args) {

        Country country = citizen.getCountry();

        if (country == null) {
            citizen.sendMessage(Translator.of("citizen.no-country"));
            return;
        }

        if (!citizen.isKing()) {
            citizen.sendMessage(Translator.of("citizen.not-king"));
            return;
        }

        citizen.sendMessage(Translator.of("country.country-removed").replace("country", country.getName()));

        country.remove();

    }

    public void createCommand(Citizen citizen, String[] args) {

        City city = citizen.getCity();

        if (city == null) {
            citizen.sendMessage(Translator.of("citizen.no-city"));
            return;
        }

        if (citizen.getCountry() == null) {
            citizen.sendMessage(Translator.of("citizen.no-country"));
            return;
        }

        if (!citizen.isKing()) {
            citizen.sendMessage(Translator.of("citizen.not-king"));
            return;
        }

        if (args.length < 2) {
            citizen.sendMessage(Translator.of("command.not-enough-args"));
            return;
        }

        Country country = CitiesAPI.getInstance().getCountry(args[1]);

        if (country != null) {
            citizen.sendMessage(Translator.of("country.creation-name-error"));
            return;
        }

        country = new Country(args[1], citizen, city);
        country.create();
        country.save();

        citizen.sendMessage(Translator.of("country.country-created").replace("country", country.getName()));

    }

}
