package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.ask.Ask;
import me.xflyiwnl.cities.object.bank.Bank;
import me.xflyiwnl.cities.object.bank.BankHandler;
import me.xflyiwnl.cities.object.bank.types.CitizenBank;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.confirmation.Confirmation;
import me.xflyiwnl.cities.database.SQLDataSource;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.invite.Invite;
import me.xflyiwnl.cities.object.rank.Rank;
import me.xflyiwnl.cities.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Citizen extends CitiesObject implements BankHandler, Saveable, Inviteable {

    private Bank bank = new CitizenBank(this);
    private City city;

    private Rank rank;

    private Ask ask;
    private Confirmation confirmation;
    private Invite invite;

    private String registered = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    private String joinedCity;

    public Citizen() {}

    public Citizen(UUID uuid) {
        super("", uuid);
    }

    public Citizen(UUID uuid, City city) {
        super(uuid);
        this.city = city;
    }

    public Citizen(UUID uuid, City city, Rank rank, String registered, String joinedCity) {
        super(uuid);
        this.city = city;
        this.rank = rank;
        this.registered = registered;
        this.joinedCity = joinedCity;
    }

    public void sendMessage(String text) {
        if (getPlayer() != null) {
            getPlayer().sendMessage(TextUtil.colorize(text));
        }
    }

    public boolean isOnline() {
        return getPlayer() != null && getPlayer().isOnline();
    }

    public boolean hasCity() {
        return city == null;
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
        Cities.getInstance().getCitizens().add(this);
    }

    @Override
    public void save() {
        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getCitizenDAO().save(this);
    }

    @Override
    public void remove() {
        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getCitizenDAO().remove(this);
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

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getJoinedCity() {
        return joinedCity;
    }

    public void setJoinedCity(String joinedCity) {
        this.joinedCity = joinedCity;
    }

    public Ask getAsk() {
        return ask;
    }

    public void setAsk(Ask ask) {
        this.ask = ask;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }
}
