package me.xflyiwnl.cities.object.command;

import me.xflyiwnl.cities.object.Citizen;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface CitiesExecutor {

    default boolean onCommand(@NotNull Citizen citizen, @NotNull String[] args) {
        return false;
    }

}
