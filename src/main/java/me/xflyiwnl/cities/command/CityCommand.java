package me.xflyiwnl.cities.command;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.confirmation.Confirmation;
import me.xflyiwnl.cities.object.*;
import me.xflyiwnl.cities.object.invite.CityInvite;
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
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        switch (args[0].toLowerCase()) {
            case "bank":
                bankCommand(citizen, args);
                break;
            case "land":
                landCommand(citizen, args);
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
                removeCity(citizen);
                break;
            case "leave":
                leaveCommand(citizen, args);
                break;
            case "new":
                createCity(citizen, args);
                break;
            case "create":
                createCity(citizen, args);
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

    public void kickCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("city.no-city")
                    .run();
            return;
        }

        if (args.length < 2) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
        }

        Citizen receiver = Cities.getInstance().getCitizen(args[1]);

        if (receiver == null) {
            Translator.send(citizen)
                    .path("citizen.unknown-citizen")
                    .run();
            return;
        }

        citizen.getCity().kickCitizen(citizen, receiver);

    }

    public void leaveCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("city.no-city")
                    .run();
            return;
        }

        citizen.getCity().leaveCitizen(citizen);

    }

    public void inviteCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("city.no-city")
                    .run();
            return;
        }

        if (args.length < 2) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
        }

        Citizen receiver = Cities.getInstance().getCitizen(args[1]);

        if (receiver == null) {
            Translator.send(citizen)
                    .path("citizen.unknown-citizen")
                    .run();
            return;
        }

        CityInvite invite = new CityInvite(
                citizen.getCity(),
                citizen, receiver
        );
        receiver.setInvite(invite);

    }

    public void broadcastCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("city.no-city")
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
        for (int i = 2; i < args.length; i++) {
            sb.append(i + 1 == args.length ? args[i] : args[i] + " ");
        }

        citizen.getCity().broadcast(sb.toString(), true);

        Translator.send(citizen)
                .path("city.broadcast-send")
                .run();

    }

    public void bankCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("city.no-city")
                    .run();
            return;
        }

        if (args.length == 1) {
            // todo open lands menu
            return;
        }

        City city = citizen.getCity();

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

                city.getBank().deposit(amount);

                city.broadcast(Translator.of("deposit-format")
                        .replace("%city%", city.getName())
                        .replace("%player%", citizen.getName())
                        .replace("%amount%", String.valueOf(amount)), false);

                break;
            case "withdraw":

                city.getBank().withdraw(amount);

                city.broadcast(Translator.of("withdraw-format")
                        .replace("%city%", city.getName())
                        .replace("%player%", citizen.getName())
                        .replace("%amount%", String.valueOf(amount)), false);

                break;
            default:
                Translator.send(citizen)
                        .path("command.unknown-arg")
                        .run();
                break;
        }
    }

    public void landCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("city.no-city")
                    .run();
            return;
        }

        if (args.length == 1) {
            // todo open lands menu
            return;
        }

        Chunk chunk = citizen.getPlayer().getChunk();
        WorldCord2 worldCord2 = new WorldCord2(chunk.getWorld(), chunk.getX(), chunk.getZ());
        Land claimed = Cities.getInstance().getLand(worldCord2);

        switch (args[1].toLowerCase()) {
            case "claim":

                if (claimed != null) {
                    Translator.send(citizen)
                            .path("land.already-claimed")
                            .replace("city", claimed.getCity().getName())
                            .run();
                    return;
                }

                Land land = new Land(
                        worldCord2, LandType.DEFAULT, citizen.getCity()
                );
                land.create(true);

                Translator.send(citizen)
                        .path("land.land-claim")
                        .replace("world", land.getCord2().getWorld().getName())
                        .replace("x", String.valueOf(land.getCord2().getX()))
                        .replace("z", String.valueOf(land.getCord2().getZ()))
                        .run();

                break;
            case "unclaim":

                if (claimed == null) {
                    Translator.send(citizen)
                            .path("land.free-land")
                            .run();
                    return;
                }

                if (!claimed.getCity().equals(citizen.getCity())) {
                    Translator.send(citizen)
                            .path("land.already-claimed")
                            .replace("city", claimed.getCity().getName())
                            .run();
                    return;
                }

                Translator.send(citizen)
                        .path("land.land-unclaim")
                        .replace("world", claimed.getCord2().getWorld().getName())
                        .replace("x", String.valueOf(claimed.getCord2().getX()))
                        .replace("z", String.valueOf(claimed.getCord2().getZ()))
                        .run();

                claimed.remove();

                break;
            default:
                Translator.send(citizen)
                        .path("command.unknown-arg")
                        .run();
                break;
        }
    }

    public void createCity(Citizen citizen, String[] args) {

        if (args.length < 2) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        if (citizen.getCity() != null) {
            Translator.send(citizen)
                    .path("city.already-has-city")
                    .replace("city", citizen.getCity().getName())
                    .run();
            return;
        }

        City city = new City(args[1], 0, citizen, null, citizen.getPlayer().getLocation());

        Chunk chunk = citizen.getPlayer().getChunk();
        WorldCord2 worldCord2 = new WorldCord2(chunk.getWorld(), chunk.getX(), chunk.getZ());

        Land land = new Land(
                worldCord2, LandType.DEFAULT, city
        );

        Confirmation confirmation = new Confirmation(
                citizen,
                Translator.of("confirmation.confirmation-messages.city-create"),
                () -> {
                    land.create(true);

                    citizen.setCity(city);
                    citizen.save();


                    city.addCitizen(citizen);
                    city.create(true);

                    Translator.send(citizen)
                            .path("city.city-created")
                            .replace("city", citizen.getCity().getName())
                            .run();
                },
                () -> {}
        );

        citizen.setConfirmation(confirmation);

    }

    public void removeCity(Citizen citizen) {

        if (citizen.getConfirmation() != null) {
            Translator.send(citizen)
                    .path("confirmation.has-confirmation")
                    .run();
            return;
        }

        City city = citizen.getCity();

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("citizen.no-city")
                    .run();
            return;
        }

        if (!citizen.isMayor()) {
            Translator.send(citizen)
                    .path("citizen.not-mayor")
                    .run();
            return;
        }

        Confirmation confirmation = new Confirmation(
                citizen,
                Translator.of("confirmation.confirmation-messages.city-remove"),
                () -> {
                    Translator.send(citizen)
                            .path("city.city-removed")
                            .replace("city", city.getName())
                            .run();

                    city.remove();
                },
                () -> {}
        );
        citizen.setConfirmation(confirmation);

    }

}
