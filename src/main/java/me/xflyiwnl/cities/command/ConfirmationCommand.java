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

public class ConfirmationCommand implements CommandExecutor, TabCompleter {

    private List<String> confirmationTabCompletes = Arrays.asList(
            "accept",
            "decline"
    );

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return confirmationTabCompletes;
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

        if (args.length == 0) {
            citizen.sendMessage(Translator.of("command.not-enough-args"));
            return true;
        }

        if (!citizen.hasConfirmation()) {
            citizen.sendMessage(Translator.of("confirmation.no-confirmations"));
            return true;
        }

        parseConfirmationCommand(citizen, args);

        return true;
    }

    public void parseConfirmationCommand(Citizen citizen, String[] args) {
        switch (args[0].toLowerCase()) {
            case "accept":
                citizen.getConfirmation().onAccept();
                break;
            case "decline":
                citizen.getConfirmation().onDecline();
                break;
            default:
                citizen.sendMessage(Translator.of("command.unknown-arg"));
                break;
        }
    }
}
