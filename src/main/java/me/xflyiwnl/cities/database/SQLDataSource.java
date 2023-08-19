package me.xflyiwnl.cities.database;

import com.wiring.api.WiringAPI;
import com.wiring.api.entity.Column;
import com.wiring.api.entity.ColumnType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.CitiesSettings;
import me.xflyiwnl.cities.database.sql.*;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.City;
import me.xflyiwnl.cities.object.Land;
import me.xflyiwnl.cities.object.Rank;

import java.util.List;


public class SQLDataSource implements CitiesDataSource {

    private WiringAPI api;

    private CountryDAO countryDAO;
    private CityDAO cityDAO;
    private CitizenDAO citizenDAO;
    private LandDAO landDAO;
    private RankDAO rankDAO;

    @Override
    public void start() {

        api = new WiringAPI(
                Cities.getInstance().getSettings().ofString("mysql.host"),
                Cities.getInstance().getSettings().ofInt("mysql.port"),
                Cities.getInstance().getSettings().ofString("mysql.user"),
                Cities.getInstance().getSettings().ofString("mysql.password"),
                null);


        String database = Cities.getInstance().getSettings().ofString("mysql.database");
        if (!api.existsDatabase(database)) {
            api.createDatabase(database)
                    .execute();
        }

        countryDAO = new CountryDAO(api, database);
        cityDAO = new CityDAO(api, database);
        citizenDAO = new CitizenDAO(api, database);
        landDAO = new LandDAO(api, database);
        rankDAO = new RankDAO(api, database);

        loadAll();
    }

    @Override
    public void end() {
        saveAll();
        api.close();
    }

    @Override
    public void saveAll() {

    }

    @Override
    public void loadAll() {
        Cities.getInstance().getCitizens().addAll(citizenDAO.all());

        Cities.getInstance().getCities().addAll(cityDAO.all());
        Cities.getInstance().getCities().forEach(city -> {
            for (Citizen citizen : city.getCitizens()) {
                citizen.setCity(city);
            }
        });

        Cities.getInstance().getLands().addAll(landDAO.all());

        Cities.getInstance().getRanks().addAll(rankDAO.all());
        Cities.getInstance().getRanks().forEach(rank -> {
            for (Citizen citizen : rank.getCitizens()) {
                citizen.setRank(rank);
            }
        });

    }

    public CountryDAO getCountryDAO() {
        return countryDAO;
    }

    public CityDAO getCityDAO() {
        return cityDAO;
    }

    public CitizenDAO getCitizenDAO() {
        return citizenDAO;
    }

    public LandDAO getLandDAO() {
        return landDAO;
    }

    public WiringAPI getApi() {
        return api;
    }

    public RankDAO getRankDAO() {
        return rankDAO;
    }
}
