package me.xflyiwnl.cities.database;

import com.wiring.api.WiringAPI;
import com.wiring.api.entity.Column;
import com.wiring.api.entity.ColumnType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.database.sql.CitizenDAO;
import me.xflyiwnl.cities.database.sql.CityDAO;
import me.xflyiwnl.cities.database.sql.CountryDAO;
import me.xflyiwnl.cities.database.sql.LandDAO;
import me.xflyiwnl.cities.object.Citizen;


public class SQLDataSource implements CitiesDataSource {

    private WiringAPI api;

    private CountryDAO countryDAO;
    private CityDAO cityDAO;
    private CitizenDAO citizenDAO;
    private LandDAO landDAO;

    @Override
    public void start() {

        api = new WiringAPI("localhost", "root", "1234");

        api.createDatabase("cities")
                .execute();

        countryDAO = new CountryDAO(api);
        cityDAO = new CityDAO(api);
        citizenDAO = new CitizenDAO(api);
        landDAO = new LandDAO(api);

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

}
