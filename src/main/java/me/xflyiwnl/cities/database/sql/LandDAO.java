package me.xflyiwnl.cities.database.sql;

import com.zaxxer.hikari.HikariDataSource;
import me.xflyiwnl.cities.object.CitiesObject;
import me.xflyiwnl.cities.object.Land;

public class LandDAO implements CitiesDAO<Land> {

    private HikariDataSource dataSource;

    public LandDAO(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void get(Land object) {

    }

    @Override
    public void save(Land object) {

    }

    @Override
    public void remove(Land object) {

    }

    @Override
    public void all(Land object) {

    }
}
