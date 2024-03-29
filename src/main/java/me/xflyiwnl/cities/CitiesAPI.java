package me.xflyiwnl.cities;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Government;
import me.xflyiwnl.cities.object.WorldCord2;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.land.Land;
import me.xflyiwnl.cities.object.rank.Rank;
import me.xflyiwnl.cities.object.tool.ToolAction;
import me.xflyiwnl.cities.object.tool.ToolBar;
import me.xflyiwnl.cities.object.tool.ToolBoard;
import me.xflyiwnl.cities.object.tool.ToolRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class CitiesAPI {

    private static CitiesAPI instance;
    private Cities cities;

    public CitiesAPI(Cities cities) {
        this.cities = cities;
        this.instance = this;
    }

    /**
     *      Citizen methods
     **/

    public Map<UUID, Citizen> getCitizens() {
        return cities.getCitizens();
    }

    public Citizen getCitizen(UUID uniqueId) {
        return getCitizens().get(uniqueId);
    }

    public Citizen getCitizen(Player player) {
        return getCitizen(player.getUniqueId());
    }

    public Citizen getCitizen(String name) {
        return getCitizen(Bukkit.getOfflinePlayer(name).getUniqueId());
    }

    /**
     *      City methods
     **/

    public Map<UUID, City> getCities() {
        return cities.getCities();
    }

    public City getCity(UUID uniqueId) {
        return getCities().get(uniqueId);
    }

    @Deprecated
    public City getCity(String name) {
        return getCities().values().stream()
                .filter(city -> city.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public City getCityByChunkCord(WorldCord2 cord2) {
        Land land = getLandByCord(cord2);

        if (land == null) return null;
        if (!land.hasCity()) return null;

        return land.getCity();
    }

    public City getCityByChunk(Chunk chunk) {
        return getCityByChunkCord(new WorldCord2(chunk));
    }

    public City getCityByCitizen(Citizen citizen) {
        return getCityByChunkCord(citizen.getChunkCord());
    }

    public City getCityByCord(WorldCord2 cord2) {
        return getCityByChunk(cord2.getChunk());
    }

    public City getCityByLocation(Location location) {
        return getCityByCord(new WorldCord2(location));
    }

    /**
     *      Country methods
     **/

    public Map<UUID, Country> getCountries() {
        return cities.getCountries();
    }

    public Country getCountry(UUID uniqueId) {
        return getCountries().get(uniqueId);
    }

    @Deprecated
    public Country getCountry(String name) {
        return getCountries().values().stream()
                .filter(country -> country.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    /**
     *      Land methods
     **/

    public Map<WorldCord2, Land> getLands() {
        return cities.getLands();
    }

    public Land getLandByCord(WorldCord2 cord2) {
        return getLands().get(cord2);
    }

    public Land getLandByChunk(Chunk chunk) {
        return getLandByCord(new WorldCord2(chunk));
    }

    public Land getLandByLocation(Location location) {
        return getLandByChunk(location.getChunk());
    }


    /**
     *      Rank methods
     **/

    public Map<UUID, Map<UUID, Rank>> getRanks() {
        return cities.getRanks();
    }

    public Map<UUID, Rank> getRanksById(UUID uniqueId) {
        return getRanks().get(uniqueId);
    }

    public Map<UUID, Rank> getRanksByGovernment(Government government) {
        return getRanksById(government.getUniqueId());
    }

    public Rank getRank(UUID governmentId, UUID rankId) {
        Map<UUID, Rank> ranks = getRanksById(governmentId);
        return ranks == null ? null : ranks.get(rankId);
    }

    @Deprecated
    public Rank getRank(UUID governmentId, String title) {
        Map<UUID, Rank> ranks = getRanksById(governmentId);
        return ranks == null ? null : ranks.values().stream()
                .filter(rank -> rank.getTitle().equalsIgnoreCase(title))
                .findFirst().orElse(null);
    }

    public Rank getRank(Government government, UUID uniqueId) {
        return getRank(government.getUniqueId(), uniqueId);
    }


    public Rank getRank(Government government, String title) {
        return getRank(government.getUniqueId(), title);
    }

    @Deprecated
    public Rank getRank(UUID uniqueId) {
        for (Map<UUID, Rank> ranks : getRanks().values()) {
            for (Rank rank : ranks.values()) {
                if (rank.getUniqueId().equals(uniqueId)){
                    return rank;
                }
            }
        }
        return null;
    }

    /**
     *      Bar methods
     **/

    public List<ToolBar> getBars() {
        return cities.getBars();
    }

    public ToolBar createBar(String text, float progress, BarColor color, BarStyle style) {
        ToolBar bar = new ToolBar(text, progress, color, style);
        getBars().add(bar);
        return bar;
    }

    public ToolBar createBar() {
        ToolBar bar = new ToolBar();
        getBars().add(bar);
        return bar;
    }

    /**
     *      Board methods
     **/

    public List<ToolBoard> getBoards() {
        return cities.getBoards();
    }

    public ToolBoard createBoard(String title) {
        ToolBoard board = new ToolBoard(title);
        getBoards().add(board);
        return board;
    }

    public ToolBoard createBoard() {
        ToolBoard board = new ToolBoard();
        getBoards().add(board);
        return board;
    }

    public ToolRunnable createRunnable(ToolAction action, int time) {
        ToolRunnable runnable = new ToolRunnable(action, time);
        return runnable;
    }

    public static CitiesAPI getInstance() {
        return instance;
    }

}
