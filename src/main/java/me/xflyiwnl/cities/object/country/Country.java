package me.xflyiwnl.cities.object.country;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Government;
import me.xflyiwnl.cities.object.Saveable;
import me.xflyiwnl.cities.object.bank.Bank;
import me.xflyiwnl.cities.object.city.City;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Country extends Government implements CitiesList, Saveable {

    private Citizen mayor;
    private City capital;

    private Map<UUID, City> cities = new HashMap<>();

    public Country() {
    }

    public Country(String name, Citizen mayor, City capital) {
        super(name);
        this.mayor = mayor;
        this.capital = capital;
    }

    public void broadcast(String message, boolean formatted) {
        cities.values().forEach(city -> {
            city.broadcast(message, formatted);
        });
    }

    @Override
    public void create() {
        Cities.getInstance().getCountries().add(this);
    }

    @Override
    public void save() {
    }

    @Override
    public void remove() {

        cities.values().forEach(city -> {
            city.setCountry(null);
            city.save();
        });

        Cities.getInstance().getCountries().remove(this);
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

    public void setCities(Map<UUID, City> cities) {
        this.cities = cities;
    }

    @Override
    public Map<UUID, City> getCities() {
        return cities;
    }


}
