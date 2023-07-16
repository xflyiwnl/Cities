package me.xflyiwnl.cities.database.sql;

import com.zaxxer.hikari.HikariDataSource;
import me.xflyiwnl.cities.object.CitiesObject;
import me.xflyiwnl.cities.object.City;

public class CityDAO implements CitiesDAO<City> {

    private HikariDataSource dataSource;

    public CityDAO(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void get(City object) {

    }

    @Override
    public void save(City object) {

    }

    @Override
    public void remove(City object) {

    }

    @Override
    public void all(City object) {

    }
}