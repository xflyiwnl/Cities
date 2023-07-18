package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.database.SQLDataSource;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Citizen extends CitiesObject implements BankHandler, Saveable {

    private Bank bank = new Bank(this, BankType.CITIZEN);
    private City city;

    public Citizen() {}

    public Citizen(UUID uuid) {
        super("", uuid);
    }

    public Citizen(UUID uuid, City city) {
        super(uuid);
        this.city = city;
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

}
