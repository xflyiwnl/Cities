package me.xflyiwnl.cities.command;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Translator;
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

        parseCountry(citizen, args);

        return true;
    }

    public void parseCountry(Citizen citizen, String[] args) {

        if (args.length == 0) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        switch (args[0].toLowerCase()) {
            case "bank":
                break;
            case "rank":
                break;
            case "online":
                break;
            case "broadcast":
                break;
            case "kick":
                break;
            case "invite":
                break;
            case "add":
                break;
            case "remove":
                break;
            case "leave":
                break;
            case "new":
                break;
            case "create":
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

}
