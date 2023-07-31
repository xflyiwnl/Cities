package me.xflyiwnl.cities.command;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.gui.CityOnlineGUI;
import me.xflyiwnl.cities.object.confirmation.Confirmation;
import me.xflyiwnl.cities.object.*;
import me.xflyiwnl.cities.object.invite.CityInvite;

import org.bukkit.Chunk;
import org.bukkit.Location;
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
            "remove",
            "spawn",
            "set"
    );

    private List<String> citySetTabCompletes = Arrays.asList(
            "name",
            "board",
            "spawn"
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
                case ("set"):
                    return citySetTabCompletes;
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
                onlineCommand(citizen, args);
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
            case "spawn":
                spawnCommand(citizen, args);
                break;
            case "set":
                setCommand(citizen, args);
                break;
            default:
                Translator.send(citizen)
                        .path("command.unknown-arg")
                        .run();
                break;
        }

    }

    public void setCommand(Citizen citizen, String[] args) {

        if (args.length < 2) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        switch (args[1].toLowerCase()) {
            case "name":
                setNameCommand(citizen, args);
                break;
            case "board":
                setBoardCommand(citizen, args);
                break;
            case "spawn":
                setSpawnCommand(citizen, args);
                break;
            default:
                Translator.send(citizen)
                        .path("command.unknown-arg")
                        .run();
                break;
        }

    }

    public void setNameCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("citizen.no-city")
                    .run();
            return;
        }

        if (args.length < 3) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        City city = citizen.getCity();

        city.setName(args[2]);
        city.save();

        Translator.send(citizen)
                .path("city.set.name")
                .replace("name", args[2])
                .run();

    }

    public void setBoardCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("citizen.no-city")
                    .run();
            return;
        }

        if (args.length < 3) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            sb.append(i + 1 == args.length ? args[i] : args[i] + " ");
        }

        citizen.getCity().setBoard(sb.toString());

        Translator.send(citizen)
                .path("city.set.board")
                .replace("board", sb.toString())
                .run();

    }

    public void setSpawnCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("citizen.no-city")
                    .run();
            return;
        }

        City city = citizen.getCity();

        Location spawn = citizen.getPlayer().getLocation();

        Chunk chunk = spawn.getChunk();
        WorldCord2 cord2 = new WorldCord2(chunk.getWorld(), chunk.getX(), chunk.getZ());
        Land land = Cities.getInstance().getLand(cord2);

        if (land == null || !land.getCity().equals(city)) {
            Translator.send(citizen)
                    .path("city.far-spawn")
                    .run();
            return;
        }

        Land currentSpawn = city.getSpawnLand();
        currentSpawn.setSpawnLand(false);

        city.setSpawn(spawn);
        city.setSpawnLand(land);
        land.setSpawnLand(true);


        currentSpawn.save();
        city.save();
        land.save();

        Translator.send(citizen)
                .path("city.set.spawn")
                .run();

    }

    public void onlineCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("citizen.no-city")
                    .run();
            return;
        }

        City city = citizen.getCity();

        CityOnlineGUI.showGUI(citizen.getPlayer(), city);

    }

    public void spawnCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("citizen.no-city")
                    .run();
            return;
        }

        City city = citizen.getCity();

        citizen.getPlayer().teleport(city.getSpawn());

        Translator.send(citizen)
                .path("citizen.teleported-spawn")
                .run();

    }

    public void kickCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("citizen.no-city")
                    .run();
            return;
        }

        if (args.length < 2) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        Citizen receiver = Cities.getInstance().getCitizen(args[1]);

        if (receiver == null) {
            Translator.send(citizen)
                    .path("citizen.unknown-citizen")
                    .run();
            return;
        }

        if (receiver.equals(citizen)) {
            Translator.send(citizen)
                    .path("citizen.kick-self")
                    .run();
            return;
        }

        if (!receiver.hasCity()) {
            Translator.send(citizen)
                    .path("citizen.kick-error")
                    .run();
            return;
        }

        if (!receiver.getCity().equals(citizen.getCity())) {
            Translator.send(citizen)
                    .path("citizen.kick-error")
                    .run();
            return;
        }

        if (receiver.isMayor()) {
            Translator.send(citizen)
                    .path("citizen.kick-mayor")
                    .run();
            return;
        }

        City city = citizen.getCity();

        city.kickCitizen(citizen, receiver);
        city.save();

    }

    public void leaveCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("citizen.no-city")
                    .run();
            return;
        }

        if (citizen.isMayor()) {
            Translator.send(citizen)
                    .path("city.mayor-leave-city")
                    .run();
            return;
        }

        City city = citizen.getCity();

        city.leaveCitizen(citizen);
        city.save();

    }

    public void inviteCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("citizen.no-city")
                    .run();
            return;
        }

        if (args.length < 2) {
            Translator.send(citizen)
                    .path("command.not-enough-args")
                    .run();
            return;
        }

        Citizen receiver = Cities.getInstance().getCitizen(args[1]);

        if (receiver == null) {
            Translator.send(citizen)
                    .path("citizen.unknown-citizen")
                    .run();
            return;
        }

        if (receiver.equals(citizen)) {
            Translator.send(citizen)
                    .path("invite.invite-self")
                    .run();
            return;
        }

        if (receiver.hasCity()) {
            Translator.send(citizen)
                    .path("invite.receiver-has.city")
                    .replace("city", receiver.getCity().getName())
                    .run();
            return;
        }

        if (receiver.hasInvite()) {
            Translator.send(citizen)
                    .path("invite.receiver-has-invite")
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
        for (int i = 1; i < args.length; i++) {
            sb.append(i + 1 == args.length ? args[i] : args[i] + " ");
        }

        citizen.getCity().broadcast(sb.toString(), true);

        Translator.send(citizen)
                .path("citizen.broadcast-send")
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

                if (citizen.getBank().current() < amount) {
                    Translator.send(citizen)
                            .path("economy.not-enough-money.citizen")
                            .run();
                    return;
                }

                citizen.getBank().pay(city.getBank(), amount);

                city.broadcast(Translator.of("economy.deposit-format")
                        .replace("%name%", city.getName())
                        .replace("%player%", citizen.getName())
                        .replace("%amount%", String.valueOf(amount)), false);

                break;
            case "withdraw":

                if (city.getBank().current() < amount) {
                    Translator.send(citizen)
                            .path("economy.not-enough-money.city")
                            .run();
                    return;
                }

                city.getBank().pay(citizen.getBank(), amount);

                city.broadcast(Translator.of("economy.withdraw-format")
                        .replace("%name%", city.getName())
                        .replace("%player%", citizen.getName())
                        .replace("%amount%", String.valueOf(amount)), false);

                break;
            default:
                Translator.send(citizen)
                        .path("command.unknown-arg")
                        .run();
                break;
        }

        city.save();

    }

    public void landCommand(Citizen citizen, String[] args) {

        if (!citizen.hasCity()) {
            Translator.send(citizen)
                    .path("citizen.no-city")
                    .run();
            return;
        }

        if (args.length == 1) {
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

                if (!land.connected()) {
                    Translator.send(citizen)
                            .path("land.not-connected")
                            .run();
                    return;
                }

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

                if (claimed.isSpawnLand()) {
                    Translator.send(citizen)
                            .path("land.unclaim-spawnland")
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

        citizen.save();

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

        if (Cities.getInstance().getCity(args[1]) != null) {
            Translator.send(citizen)
                    .path("city.creation-name-error")
                    .run();
            return;
        }

        City landCity = Cities.getInstance().getCityByLand(citizen);

        if (landCity != null) {
            Translator.send(citizen)
                    .path("land.already-claimed")
                    .replace("city", landCity.getName())
                    .run();
            return;
        }

        City city = new City(args[1], 0, citizen, null, citizen.getPlayer().getLocation(), Cities.getInstance().getSettings().ofString("board.default"));

        Chunk chunk = citizen.getPlayer().getChunk();
        WorldCord2 worldCord2 = new WorldCord2(chunk.getWorld(), chunk.getX(), chunk.getZ());

        Land land = new Land(
                worldCord2, LandType.DEFAULT, city
        );

        city.setSpawnLand(land);
        land.setSpawnLand(true);

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
