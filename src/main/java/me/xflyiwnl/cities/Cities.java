package me.xflyiwnl.cities;

import me.xflyiwnl.cities.database.CitiesDatabase;
import me.xflyiwnl.cities.database.DatabaseType;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.City;
import me.xflyiwnl.cities.object.Country;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Cities extends JavaPlugin {

    private static Cities instance;

    private CitiesDatabase database;

    private List<Country> countries = new ArrayList<Country>();
    private List<City> cities = new ArrayList<City>();
    private List<Citizen> citizens = new ArrayList<Citizen>();

    @Override
    public void onEnable() {

        instance = this;

        database = new CitiesDatabase(DatabaseType.SQL);

    }

    @Override
    public void onDisable() {
        database.getSource().end();
    }

    public Citizen getCitizen(Player player) {
        return getCitizen(player.getUniqueId());
    }

    public Citizen getCitizen(UUID uuid) {
        for (Citizen citizen : citizens) {
            if (citizen.getUniqueId().equals(uuid)) {
                return citizen;
            }
        }
        return null;
    }

    public Citizen getCitizen(String name) {
        for (Citizen citizen : citizens) {
            if (citizen.getName().equals(name)) {
                return citizen;
            }
        }
        return null;
    }

    public City getCity(UUID uuid) {
        for (City city : cities) {
            if (city.getUniqueId().equals(uuid)) {
                return city;
            }
        }
        return null;
    }

    public City getCity(String name) {
        for (City city : cities) {
            if (city.getName().equals(name)) {
                return city;
            }
        }
        return null;
    }

    public Country getCountry(UUID uuid) {
        for (Country country : countries) {
            if (country.getUniqueId().equals(uuid)) {
                return country;
            }
        }
        return null;
    }

    public Country getCountry(String name) {
        for (Country country : countries) {
            if (country.getName().equals(name)) {
                return country;
            }
        }
        return null;
    }

    public CitiesDatabase getDatabase() {
        return database;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public List<City> getCities() {
        return cities;
    }

    public List<Citizen> getCitizens() {
        return citizens;
    }

    public static Cities getInstance() {
        return instance;
    }

    public static void setInstance(Cities instance) {
        Cities.instance = instance;
    }

}
