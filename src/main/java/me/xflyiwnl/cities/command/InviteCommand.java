package me.xflyiwnl.cities.command;

import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.object.citizen.Citizen;
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

public class InviteCommand implements CommandExecutor, TabCompleter {

    private List<String> inviteTabCompletes = Arrays.asList(
            "accept",
            "decline"
    );

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) {
            return inviteTabCompletes;
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

        if (args.length == 0) {
            citizen.sendMessage(Translator.of("command.not-enough-args"));
            return true;
        }

        if (!citizen.hasInvite()) {
            citizen.sendMessage(Translator.of("invite.no-invite"));
            return true;
        }

        parseInviteCommand(citizen, args);

        return true;
    }

    public void parseInviteCommand(Citizen citizen, String[] args) {
        switch (args[0].toLowerCase()) {
            case "accept":
                citizen.getInvite().accept();
                break;
            case "decline":
                citizen.getInvite().decline();
                break;
            default:
                citizen.sendMessage(Translator.of("command.unknown-arg"));
                break;
        }
    }
}
