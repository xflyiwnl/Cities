package me.xflyiwnl.cities;

import me.xflyiwnl.cities.command.CityCommand;
import me.xflyiwnl.cities.database.CitiesDatabase;
import me.xflyiwnl.cities.database.DatabaseType;
import me.xflyiwnl.cities.listener.PlayerListener;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.City;
import me.xflyiwnl.cities.object.Country;
import me.xflyiwnl.cities.object.Land;
import me.xflyiwnl.cities.object.timer.ActionTimer;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Cities extends JavaPlugin {

    private static Cities instance;

    private CitiesDatabase database;
    private Economy economy;

    private List<Country> countries = new ArrayList<Country>();
    private List<City> cities = new ArrayList<City>();
    private List<Citizen> citizens = new ArrayList<Citizen>();

    @Override
    public void onEnable() {

        instance = this;

        if (!setupEconomy() ) {
            Bukkit.getPluginManager().disablePlugin(Cities.getInstance());
            return;
        }

        database = new CitiesDatabase(DatabaseType.SQL);


        for (Player player : Bukkit.getOnlinePlayers()) {
            Citizen citizen = Cities.getInstance().getCitizen(player);

            if (citizen == null) {
                citizen = new Citizen(player.getUniqueId());
                citizen.create(true);
                System.out.println("create");
            }
        }

        ActionTimer timer = new ActionTimer( 20);

        registerCommands();
        registerListeners();

    }

    @Override
    public void onDisable() {
        database.getSource().end();
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public void registerCommands() {
        getCommand("city").setExecutor(new CityCommand());
        getCommand("city").setTabCompleter(new CityCommand());
    }

    public City getCityByLand(Citizen citizen) {
        for (City city : cities) {
            for (Land land : city.getLands()) {
                if (land.getX() == citizen.getPlayer().getChunk().getX() &&
                        land.getZ() == citizen.getPlayer().getChunk().getZ()) {
                    return city;
                }
            }
        }
        return null;
    }

    public City getCityByLand(Player player) {
        for (City city : cities) {
            for (Land land : city.getLands()) {
                if (land.getX() == player.getChunk().getX() &&
                        land.getZ() == player.getChunk().getZ()) {
                    return city;
                }
            }
        }
        return null;
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

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
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

    public Economy getEconomy() {
        return economy;
    }
}
