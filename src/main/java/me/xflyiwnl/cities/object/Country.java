package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.database.SQLDataSource;

import java.util.ArrayList;
import java.util.List;

public class Country extends Government implements CitiesList {

    private Citizen mayor;
    private City capital;

    private List<City> cities = new ArrayList<City>();

    public Country(String name) {
        super(name);
    }

    @Override
    public void save() {
        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getCountryDAO().save(this);
    }

    @Override
    public void remove() {
        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getCountryDAO().remove(this);
    }

    public Citizen getMayor() {
        return mayor;
    }

    public void setMayor(Citizen mayor) {
        this.mayor = mayor;
    }

    public City getCapital() {
        return capital;
    }

    public void setCapital(City capital) {
        this.capital = capital;
    }

    @Override
    public Bank getBank() {
        return super.getBank();
    }

    @Override
    public void setBank(Bank bank) {
        super.setBank(bank);
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public List<City> getCities() {
        return cities;
    }

    @Override
    public void addCity(City city) {
        CitiesList.super.addCity(city);
    }

    @Override
    public void removeCity(City city) {
        CitiesList.super.removeCity(city);
    }

}
