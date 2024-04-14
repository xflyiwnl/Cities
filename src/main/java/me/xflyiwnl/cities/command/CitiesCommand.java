package me.xflyiwnl.cities.command;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.util.Translator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CitiesCommand implements CommandExecutor, TabCompleter {

    private List<String> arguments = Arrays.asList(
            "time"
    );

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1) {
            return arguments;
        }

        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        parseCommand(sender, args);
        return true;
    }

    public void parseCommand(CommandSender sender, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(Translator.of("command.not-enough-args"));
            return;
        }

        switch (args[0].toLowerCase()) {
            case "time" -> {
                timeCommand(sender, args);
            }
            default -> {
                sender.sendMessage(Translator.of("command.unknown-arg"));
            }
        }

    }

    public void timeCommand(CommandSender sender, String[] args) {

        Cities instance = Cities.getInstance();

        LocalDateTime lastDay = instance.getLastDay();
        int[] dayTime = instance.dayTime();

        LocalDateTime nextDay = lastDay.plusHours(dayTime[0])
                .plusMinutes(dayTime[1]);

        Duration between = Duration.between(LocalDateTime.now(), nextDay);

        sender.sendMessage(Translator.of("time.command")
                .replace("%hours%", String.valueOf(between.toHours()))
                .replace("%minutes%", String.valueOf(between.toMinutesPart())));

    }

}
