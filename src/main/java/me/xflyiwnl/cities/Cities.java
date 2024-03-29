package me.xflyiwnl.cities;

import me.xflyiwnl.cities.command.CityCommand;
import me.xflyiwnl.cities.command.ConfirmationCommand;
import me.xflyiwnl.cities.command.CountryCommand;
import me.xflyiwnl.cities.command.InviteCommand;
import me.xflyiwnl.cities.database.CitiesDatabase;
import me.xflyiwnl.cities.database.DatabaseType;
import me.xflyiwnl.cities.dynmap.DynmapDrawer;
import me.xflyiwnl.cities.listener.PlayerListener;
import me.xflyiwnl.cities.object.*;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.land.Land;
import me.xflyiwnl.cities.object.rank.Rank;
import me.xflyiwnl.cities.timer.DynmapTimer;
import me.xflyiwnl.cities.timer.PacketTimer;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Cities extends JavaPlugin {

    private static Cities instance;

    private CitiesDatabase database;
    private FileManager fileManager;
    private CitiesSettings settings;
    private Economy economy;
    private DynmapDrawer dynmapDrawer;
    private ColorfulGUI colorfulGUI;

    private List<Country> countries = new ArrayList<Country>();
    private List<City> cities = new ArrayList<City>();
    private List<Citizen> citizens = new ArrayList<Citizen>();
    private List<Land> lands = new ArrayList<Land>();
    private List<Rank> ranks = new ArrayList<Rank>();

    @Override
    public void onEnable() {

        instance = this;

        if (!setupEconomy() ) {
            Bukkit.getPluginManager().disablePlugin(Cities.getInstance());
            return;
        }

        colorfulGUI = new ColorfulGUI(this);

        fileManager = new FileManager();
        fileManager.create();

        settings = new CitiesSettings(fileManager.getSettings().yaml());
        database = new CitiesDatabase(DatabaseType.SQL);

        for (Player player : Bukkit.getOnlinePlayers()) {
            Citizen citizen = Cities.getInstance().getCitizen(player);

            if (citizen == null) {
                citizen = new Citizen(player.getUniqueId());
                citizen.create();
                citizen.save();
            }
        }

//        new ActionTimer( 20);

        registerCommands();
        registerListeners();

        dynmapDrawer = new DynmapDrawer();
        try {
            dynmapDrawer.enable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        new DynmapTimer(100);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "cities:city");
        new PacketTimer(1);

    }

    @Override
    public void onDisable() {
        dynmapDrawer.disable();
        database.getSource().end();
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public void registerCommands() {
        getCommand("city").setExecutor(new CityCommand());
        getCommand("city").setTabCompleter(new CityCommand());

        getCommand("country").setExecutor(new CountryCommand());
        getCommand("country").setTabCompleter(new CountryCommand());

        getCommand("confirmation").setExecutor(new ConfirmationCommand());
        getCommand("confirmation").setTabCompleter(new ConfirmationCommand());

        getCommand("invite").setExecutor(new InviteCommand());
        getCommand("invite").setTabCompleter(new InviteCommand());
    }

    public City getCityByLand(Citizen citizen) {
        return getCityByLand(citizen.getPlayer());
    }

    public City getCityByLand(Player player) {
        Chunk chunk = player.getChunk();
        Land land = getLand(new WorldCord2(chunk.getWorld(), chunk.getX(), chunk.getZ()));
        if (land != null) {
            return land.getCity();
        }
        return null;
    }

    public List<Land> getCityLands(City city) {
        List<Land> lands = new ArrayList<>();
        for (Land land : this.lands) {
            if (land.getCity() != null && land.getCity().equals(city)) {
                lands.add(land);
            }
        }
        return lands;
    }

    public Land getLand(WorldCord2 cord2) {
        for (Land land : lands) {
            if (land.getCord2().getWorld().getName().equals(cord2.getWorld().getName()) && land.getCord2().getX() == cord2.getX() &&
                    land.getCord2().getZ() == cord2.getZ()) {
                return land;
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

    public Rank getRank(Government government, String title) {
        for (Rank rank : ranks) {
            if (rank.getGovernment().equals(government) && rank.getTitle().equalsIgnoreCase(title)) {
                return rank;
            }
        }
        return null;
    }

    public Rank getRank(UUID uuid) {
        for (Rank rank : ranks) {
            if (rank.getUniqueId().equals(uuid)) {
                return rank;
            }
        }
        return null;
    }

    public List<Rank> getRanks(Government government) {
        List<Rank> ranks = new ArrayList<Rank>();
        for (Rank rank : this.ranks) {
            if (rank.getGovernment().equals(government)) {
                ranks.add(rank);
            }
        }
        return ranks;
    }

    public Government getGovernment(UUID uuid) {
        for (Government government : cities) {
            if (government.getUniqueId().equals(uuid)) {
                return government;
            }
        }
        for (Government government : countries) {
            if (government.getUniqueId().equals(uuid)) {
                return government;
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

    public FileManager getFileManager() {
        return fileManager;
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

    public List<Land> getLands() {
        return lands;
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

    public DynmapDrawer getDynmapDrawer() {
        return dynmapDrawer;
    }

    public ColorfulGUI getColorfulGUI() {
        return colorfulGUI;
    }

    public CitiesSettings getSettings() {
        return settings;
    }

    public List<Rank> getRanks() {
        return ranks;
    }
}
