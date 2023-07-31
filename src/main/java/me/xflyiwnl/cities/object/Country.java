package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.database.SQLDataSource;

import java.util.ArrayList;
import java.util.List;


public class Country extends Government implements CitiesList {

    private Citizen mayor;
    private City capital;

    private List<City> cities = new ArrayList<City>();

    public Country() {
    }

    public Country(String name, Citizen mayor, City capital) {
        super(name);
        this.mayor = mayor;
        this.capital = capital;
    }

    public void broadcast(String message, boolean formatted) {
        cities.forEach(city -> {
            city.broadcast(message, formatted);
        });
    }

    public void create(boolean save) {
        Cities.getInstance().getCountries().add(this);
        if (save) {
            save();
        }
    }

    @Override
    public void save() {
        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getCountryDAO().save(this);
    }

    @Override
    public void remove() {

        cities.forEach(city -> {
            city.setCountry(null);
            city.save();
        });

        Cities.getInstance().getCountries().remove(this);

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
