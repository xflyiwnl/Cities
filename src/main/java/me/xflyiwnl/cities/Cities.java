package me.xflyiwnl.cities;

import me.xflyiwnl.cities.command.CityCommand;
import me.xflyiwnl.cities.command.ConfirmationCommand;
import me.xflyiwnl.cities.command.CountryCommand;
import me.xflyiwnl.cities.command.InviteCommand;
import me.xflyiwnl.cities.dynmap.DynmapDrawer;
import me.xflyiwnl.cities.listener.PlayerListener;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.WorldCord2;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.land.Land;
import me.xflyiwnl.cities.object.rank.Rank;
import me.xflyiwnl.cities.object.tool.ToolBar;
import me.xflyiwnl.cities.object.tool.ToolBoard;
import me.xflyiwnl.cities.timer.timers.ActionTimer;
import me.xflyiwnl.cities.timer.timers.CityTimer;
import me.xflyiwnl.cities.timer.timers.DynmapTimer;
import me.xflyiwnl.cities.timer.timers.PacketTimer;
import me.xflyiwnl.colorfulgui.ColorfulGUI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Cities extends JavaPlugin {

    private static Cities instance;

    private Economy economy;
    private DynmapDrawer dynmapDrawer;
    private ColorfulGUI guiApi;
    private final CitiesAPI api = new CitiesAPI(this);
    private final FileManager fileManager = new FileManager();
    private final String channelName = "cities:city";

    private final Map<UUID, Country> countries = new HashMap<>();
    private final Map<UUID, City> cities = new HashMap<>();
    private final Map<UUID, Citizen> citizens = new HashMap<>();
    private final Map<WorldCord2, Land> lands = new HashMap<>();
    private final Map<UUID, Rank> ranks = new HashMap<>();

    private final List<ToolBoard> boards = new ArrayList<>();
    private final List<ToolBar> bars = new ArrayList<>();

    @Override
    public void onEnable() {

        instance = this;
        guiApi = new ColorfulGUI(this);

        fileManager.generate();

        if (!setupEconomy() ) {
            Bukkit.getPluginManager().disablePlugin(Cities.getInstance());
            return;
        }

        checkOnlinePlayers();

        registerCommands();
        registerListeners();
        registerDynmap();
        registerChannels();

        startTimers();

    }

    @Override
    public void onDisable() {
        if (dynmapDrawer != null) {
            dynmapDrawer.disable();
        }
    }

    public void checkOnlinePlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Citizen citizen = CitiesAPI.getInstance().getCitizen(player);

            if (citizen == null) {
                citizen = new Citizen(player.getUniqueId());
                citizen.create();
                citizen.save();
            }
        }
    }

    public void startTimers() {
        ActionTimer actionTimer = new ActionTimer();
        actionTimer.startTimer(1);

        CityTimer cityTimer = new CityTimer();
        cityTimer.startTimer(1);

        PacketTimer packetTimer = new PacketTimer();
        packetTimer.startTimer(1);

        if (dynmapDrawer != null) {
            DynmapTimer dynmapTimer = new DynmapTimer();
            dynmapTimer.startTimer(1);
        }
    }

    public void registerChannels() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, channelName);
    }

    public void registerDynmap() {
        if (Bukkit.getPluginManager().getPlugin("dynmap") != null) {
            dynmapDrawer = new DynmapDrawer();
            try {
                dynmapDrawer.enable();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new PlayerListener(), this);
    }

    public void registerCommands() {
        CityCommand cityCommand = new CityCommand();
        getCommand("city").setExecutor(cityCommand);
        getCommand("city").setTabCompleter(cityCommand);

        CountryCommand countryCommand = new CountryCommand();
        getCommand("country").setExecutor(countryCommand);
        getCommand("country").setTabCompleter(countryCommand);

        ConfirmationCommand confirmationCommand = new ConfirmationCommand();
        getCommand("confirmation").setExecutor(confirmationCommand);
        getCommand("confirmation").setTabCompleter(confirmationCommand);

        InviteCommand inviteCommand = new InviteCommand();
        getCommand("invite").setExecutor(inviteCommand);
        getCommand("invite").setTabCompleter(inviteCommand);
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

    public Map<UUID, Country> getCountries() {
        return countries;
    }

    public Map<UUID, City> getCities() {
        return cities;
    }

    public Map<UUID, Citizen> getCitizens() {
        return citizens;
    }

    public Map<WorldCord2, Land> getLands() {
        return lands;
    }

    public Map<UUID, Rank> getRanks() {
        return ranks;
    }

    public List<ToolBoard> getBoards() {
        return boards;
    }

    public List<ToolBar> getBars() {
        return bars;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

    public static Cities getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return economy;
    }

    public DynmapDrawer getDynmapDrawer() {
        return dynmapDrawer;
    }

    public ColorfulGUI getGuiApi() {
        return guiApi;
    }

    public CitiesAPI getApi() {
        return api;
    }

    public String getChannelName() {
        return channelName;
    }

}
