package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.confirmation.Confirmation;
import me.xflyiwnl.cities.database.SQLDataSource;
import me.xflyiwnl.cities.object.invite.Invite;
import me.xflyiwnl.cities.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Citizen extends CitiesObject implements BankHandler, Saveable, Inviteable {

    private Bank bank = new Bank(this, BankType.CITIZEN);
    private City city;

    private Confirmation confirmation;
    private Invite invite;

    public Citizen() {}

    public Citizen(UUID uuid) {
        super("", uuid);
    }

    public Citizen(UUID uuid, City city) {
        super(uuid);
        this.city = city;
    }

    public void sendMessage(String text) {
        if (getPlayer() != null) {
            getPlayer().sendMessage(TextUtil.colorize(text));
        }
    }

    public boolean isOnline() {
        if (getPlayer() == null) {
            return false;
        }
        if (!getPlayer().isOnline()) {
            return false;
        }
        return true;
    }

    public boolean hasCity() {
        if (city == null) {
            return false;
        }
        return true;
    }

    public boolean isMayor() {
        if (city == null) {
            return false;
        }
        if (!city.getMayor().equals(this)) {
            return false;
        }
        return true;
    }

    public boolean hasConfirmation() {
        return confirmation == null ? false : true;
    }

    public boolean hasInvite() {
        return invite == null ? false : true;
    }

    public void create(boolean save) {
        Cities.getInstance().getCitizens().add(this);
        if (save) {
            save();
        }
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
        if (getPlayer() == null) {
            return Bukkit.getOfflinePlayer(getUniqueId()).getName();
        } else {
            return getPlayer().getName();
        }
    }

    @Override
    public Bank getBank() {
        return bank;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
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

}
