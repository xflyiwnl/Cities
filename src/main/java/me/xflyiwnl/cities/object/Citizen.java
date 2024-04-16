package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.bank.Bank;
import me.xflyiwnl.cities.object.bank.BankHandler;
import me.xflyiwnl.cities.object.bank.types.CitizenBank;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.city.RankHandler;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.invite.Invite;
import me.xflyiwnl.cities.object.rank.PermissionNode;
import me.xflyiwnl.cities.object.rank.Rank;
import me.xflyiwnl.cities.object.tool.ask.Ask;
import me.xflyiwnl.cities.object.tool.confirmation.Confirmation;
import me.xflyiwnl.cities.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.*;

public class Citizen extends CitiesObject implements RankHandler, BankHandler, Saveable, Inviteable {

    private Bank bank = new CitizenBank(this);
    private City city;

    private Ask ask;
    private Confirmation confirmation;
    private Invite invite;

    private LocalDateTime registered = LocalDateTime.now();
    private LocalDateTime joinedCity;

    private Map<UUID, Rank> ranks = new HashMap<>();

    public Citizen() {}

    public Citizen(UUID uuid) {
        super("", uuid);
    }

    public Citizen(UUID uuid, City city) {
        super(uuid);
        this.city = city;
    }

    public Citizen(UUID uuid, City city, Map<UUID, Rank> ranks, LocalDateTime registered, LocalDateTime joinedCity) {
        super(uuid);
        this.city = city;
        this.ranks = ranks;
        this.registered = registered;
        this.joinedCity = joinedCity;
    }

    public void sendMessage(String text) {
        if (getPlayer() != null) {
            getPlayer().sendMessage(TextUtil.colorize(text));
        }
    }

    public boolean hasPermission(PermissionNode node) {
        for (Rank rank : getRanks().values()) {
            if (rank.hasPermission(node)) {
                return true;
            }
        }
        return false;
    }

    public WorldCord2 getChunkCord() {
        return new WorldCord2(getPlayer().getChunk());
    }

    public WorldCord2 getLocationCord() {
        return new WorldCord2(getLocation());
    }

    public Location getLocation() {
        return getPlayer().getLocation();
    }

    public boolean isOnline() {
        return getPlayer() != null && getPlayer().isOnline();
    }

    public boolean hasCity() {
        return city != null;
    }

    public boolean isMayor() {
        return city != null && city.getMayor().equals(this);
    }

    public boolean isKing() {
        return city != null && city.hasCountry() && city.getCountry().getMayor().equals(this);
    }

    public boolean hasCountry() {
        return city != null && city.hasCountry();
    }

    public Country getCountry() {
        return city == null ? null : city.getCountry();
    }

    public boolean hasAsk() {
        return ask != null;
    }

    public boolean hasConfirmation() {
        return confirmation != null;
    }

    @Override
    public boolean hasInvite() {
        return invite != null;
    }

    @Override
    public void create() {
        Cities.getInstance().getCitizens().put(getUniqueId(), this);
    }

    @Override
    public void save() {
    }

    @Override
    public void remove() {

    }

    @Override
    public Map<UUID, Rank> getRanks() {
        return ranks;
    }

    public void setRanks(Map<UUID, Rank> ranks) {
        this.ranks = ranks;
    }

    @Override
    public String getName() {
        return Bukkit.getOfflinePlayer(getUniqueId()).getName();
    }

    @Override
    public Bank getBank() {
        return bank;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUniqueId());
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Confirmation getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(Confirmation confirmation) {
        this.confirmation = confirmation;
    }

    @Override
    public Invite getInvite() {
        return invite;
    }

    public void setInvite(Invite invite) {
        this.invite = invite;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    public LocalDateTime getJoinedCity() {
        return joinedCity;
    }

    public void setJoinedCity(LocalDateTime joinedCity) {
        this.joinedCity = joinedCity;
    }

    public Ask getAsk() {
        return ask;
    }

    public void setAsk(Ask ask) {
        this.ask = ask;
    }
}
