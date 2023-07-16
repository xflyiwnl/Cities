package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.database.SQLDataSource;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class City extends Government implements CitizenList, Spawnable, Claimable {

    private Citizen mayor;
    private Country country;

    private Location spawn;

    private List<Citizen> citizens = new ArrayList<Citizen>();
    private List<Land> lands = new ArrayList<Land>();

    public City(String name) {
        super(name);
    }

    public City(String name, Citizen mayor, Country country, Location spawn) {
        super(name);
        this.mayor = mayor;
        this.country = country;
        this.spawn = spawn;
    }

    @Override
    public void save() {
        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getCityDAO().save(this);
    }

    @Override
    public void remove() {
        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getCityDAO().remove(this);
    }

    @Override
    public void claimLand(Land land) {
        Claimable.super.claimLand(land);
    }

    @Override
    public void unclaimLand(Land land) {
        Claimable.super.unclaimLand(land);
    }

    @Override
    public List<Land> getLands() {
        return lands;
    }

    public void setLands(List<Land> lands) {
        this.lands = lands;
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
    public List<Citizen> getCitizens() {
        return citizens;
    }

    public void setCitizens(List<Citizen> citizens) {
        this.citizens = citizens;
    }

    @Override
    public void addCitizen(Citizen citizen) {
        CitizenList.super.addCitizen(citizen);
    }

    @Override
    public void removeCitizen(Citizen citizen) {
        CitizenList.super.removeCitizen(citizen);
    }

}
