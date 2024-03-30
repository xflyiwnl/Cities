package me.xflyiwnl.cities.object.city;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.object.*;
import me.xflyiwnl.cities.object.bank.Bank;
import me.xflyiwnl.cities.object.bank.types.GovernmentBank;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.invite.Invite;
import me.xflyiwnl.cities.object.land.Land;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class City extends Government implements CitizenList, Spawnable, Claimable, Inviteable, Saveable {

    private Citizen mayor;
    private Country country;

    private Location spawn;
    private Land spawnLand;

    private Invite invite;
    private String board;

    private Map<UUID, Citizen> citizens = new HashMap<>();
    private Map<WorldCord2, Land> lands = new HashMap<>();

    public City() {
        super();
    }

    public City(String name) {
        super(name);
    }

    public City(String name, double bank, Citizen mayor, Country country, Location spawn) {
        super(name);
        setBank(new GovernmentBank(this, bank));
        this.mayor = mayor;
        this.country = country;
        this.spawn = spawn;
    }

    public City(String name, double bank, Citizen mayor, Country country, Location spawn, String board) {
        super(name);
        setBank(new GovernmentBank(this, bank));
        this.mayor = mayor;
        this.country = country;
        this.spawn = spawn;
        this.board = board;
    }

    public City(String name, UUID uuid, double bank, Citizen mayor, Country country, Location spawn, String board) {
        super(name, uuid);
        setBank(new GovernmentBank(this, bank));
        this.mayor = mayor;
        this.country = country;
        this.spawn = spawn;
        this.board = board;
    }

    @Override
    public void create() {
        Cities.getInstance().getCities().put(getUniqueId(), this);
    }

    @Override
    public void save() {
    }

    @Override
    public void remove() {

        for (Citizen citizen : citizens.values()) {
            citizen.setCity(null);
            citizen.setJoinedCity(null);
            citizen.save();
        }

        for (Land land : getLands().values()) {
            land.remove();
        }

        Cities.getInstance().getCities().remove(getUniqueId());
    }

    public void broadcast(String message, boolean format) {
        for (Citizen citizen : citizens.values()) {
            if (!citizen.isOnline()) continue;

            if (format) {
                citizen.sendMessage(Translator.of("other.broadcast-format")
                        .replace("%city%", getName())
                        .replace("%message%", message));
            } else {
                citizen.sendMessage(message);
            }
        }
    }

    public Map<WorldCord2, Land> getLands() {
        return lands;
    }

    @Override
    public void claimLand(Land land) {
        lands.put(land.getCord2(), land);
        land.setCity(this);
        land.save();
    }

    @Override
    public void unclaimLand(Land land) {
        lands.remove(land.getCord2());
        land.setCity(null);
        land.save();
    }

    public boolean hasCountry() {
        return country != null;
    }

    public boolean isCapital() {
        return hasCountry() && getCountry().getCapital().equals(this);
    }

    @Override
    public boolean hasInvite() {
        return invite != null;
    }

    public Citizen getMayor() {
        return mayor;
    }

    public void setMayor(Citizen mayor) {
        this.mayor = mayor;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;

        Chunk chunk = spawn.getChunk();
        WorldCord2 cord2 = new WorldCord2(chunk.getWorld(), chunk.getX(), chunk.getZ());
        spawnLand = CitiesAPI.getInstance().getLandByCord(cord2);
    }

    @Override
    public Bank getBank() {
        return super.getBank();
    }

    @Override
    public void setBank(Bank bank) {
        super.setBank(bank);
    }

    @Override
    public Map<UUID, Citizen> getCitizens() {
        return citizens;
    }

    public void setCitizens(Map<UUID, Citizen> citizens) {
        this.citizens = citizens;
    }

    @Override
    public void addCitizen(Citizen citizen) {
        getCitizens().put(citizen.getUniqueId(), citizen);
        citizen.setCity(this);

        citizen.setJoinedCity(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        citizen.save();
    }

    @Override
    public void removeCitizen(Citizen citizen) {

        getCitizens().remove(citizen.getUniqueId());
        citizen.setCity(null);

        citizen.save();
    }

    @Override
    public Invite getInvite() {
        return invite;
    }

    public void setInvite(Invite invite) {
        this.invite = invite;
    }

    @Override
    public Land getSpawnLand() {
        return spawnLand;
    }

    public void setSpawnLand(Land spawnLand) {
        this.spawnLand = spawnLand;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

}
