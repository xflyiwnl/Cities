package me.xflyiwnl.cities.command;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.City;
import me.xflyiwnl.cities.object.Land;
import me.xflyiwnl.cities.object.LandType;
import me.xflyiwnl.cities.object.command.CitiesExecutor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CityCommand implements CommandExecutor, TabCompleter {

    private List<String> cityTabCompletes = Arrays.asList(
            "bank",
            "land",
            "rank",
            "online",
            "broadcast",
            "kick",
            "invite",
            "add",
            "create",
            "new",
            "leave",
            "remove"
    );

    private List<String> cityBankTabCompletes = Arrays.asList(
            "deposit",
            "withdraw"
    );

    private List<String> cityLandTabCompletes = Arrays.asList(
            "claim",
            "unclaim"
    );

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            return null;
        }

        Player player = (Player) sender;
        Citizen citizen = Cities.getInstance().getCitizen(player);

        if (args.length == 1) {
            return cityTabCompletes;
        }

        if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case ("bank"):
                    return cityBankTabCompletes;
                case ("land"):
                    return cityLandTabCompletes;
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

        parseCityCommand(citizen, args);

        return true;
    }

    public void parseCityCommand(Citizen citizen, String[] args) {

        if (args.length == 0) {
            // todo status screen
            return;
        }

        switch (args[0].toLowerCase()) {
            case "bank":
                parseBankCommand(citizen, args);
                break;
            case "land":
                parseLandCommand(citizen, args);
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
                createCity(citizen, args);
                break;
            case "create":
                createCity(citizen, args);
                break;
            case "set":
                parseSetCommand(citizen, args);
                break;
            default:
                break;
        }

    }

    public boolean parseBankCommand(Citizen citizen, String[] args) {
        return true;
    }

    public boolean parseSetCommand(Citizen citizen, String[] args) {
        return true;
    }

    public boolean parseLandCommand(Citizen citizen, String[] args) {
        return true;
    }

    public boolean createCity(Citizen citizen, String[] args) {

        City city = new City(args[1], citizen, null, citizen.getPlayer().getLocation());

        Chunk chunk = citizen.getPlayer().getChunk();
        Land land = new Land(
                chunk.getWorld(), chunk.getX(), chunk.getZ(), LandType.DEFAULT, city
                );
        city.getLands().add(land);

        citizen.setCity(city);

        city.create(true);

        land.save();
        citizen.save();

        return true;
    }

}
