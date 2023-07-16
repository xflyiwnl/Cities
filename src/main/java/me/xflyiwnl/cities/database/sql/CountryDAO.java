package me.xflyiwnl.cities.database.sql;

import com.zaxxer.hikari.HikariDataSource;
import me.xflyiwnl.cities.object.CitiesObject;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Country;

public class CountryDAO implements CitiesDAO<Country> {

    private HikariDataSource dataSource;

    public CountryDAO(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void get(Country object) {

    }

    @Override
    public void save(Country object) {

    }

    @Override
    public void remove(Country object) {

    }

    @Override
    public void all(Country object) {

    }
}
