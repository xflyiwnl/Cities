package me.xflyiwnl.cities.database.sql;

import com.zaxxer.hikari.HikariDataSource;
import me.xflyiwnl.cities.object.CitiesObject;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Country;

public class CitizenDAO implements CitiesDAO<Citizen> {

    private HikariDataSource dataSource;

    public CitizenDAO(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void get(Citizen object) {

    }

    @Override
    public void save(Citizen object) {

    }

    @Override
    public void remove(Citizen object) {

    }

    @Override
    public void all(Citizen object) {

    }
}
