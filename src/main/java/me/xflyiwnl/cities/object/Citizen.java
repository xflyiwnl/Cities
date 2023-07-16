package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.database.SQLDataSource;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Citizen extends CitiesObject implements BankHandler, Saveable {

    private Bank bank = new Bank();
    private City city;

    public Citizen(String name) {
        super(name);
    }

    public Citizen(String name, City city) {
        super(name);
        this.city = city;
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
    public Bank getBank() {
        return null;
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
