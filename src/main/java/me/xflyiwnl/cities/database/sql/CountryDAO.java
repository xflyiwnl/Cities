package me.xflyiwnl.cities.database.sql;

import com.wiring.api.WiringAPI;
import com.wiring.api.entity.Column;
import com.wiring.api.entity.ColumnType;
import com.wiring.api.entity.WiringResult;
import com.zaxxer.hikari.HikariDataSource;
import me.xflyiwnl.cities.object.CitiesObject;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Country;

import java.util.List;

public class CountryDAO implements CitiesDAO<Country> {

    private WiringAPI api;

    public CountryDAO(WiringAPI api) {
        this.api = api;

        create();
    }

    @Override
    public void create() {
        if (!api.existsDatabase("cities")) {
            return;
        }
        api.getDatabase("cities")
                .createTable("countries")
                .column(new Column("uuid", ColumnType.VARCHAR).primaryKey().notNull())
                .column(new Column("name", ColumnType.VARCHAR).notNull())
                .column(new Column("bank", ColumnType.DOUBLE).notNull())
                .column(new Column("mayor", ColumnType.VARCHAR).notNull())
                .column(new Column("capital", ColumnType.VARCHAR).notNull())
                .column(new Column("countries", ColumnType.VARCHAR))
                .execute();
    }

    @Override
    public Country get(WiringResult result) {
        return null;
    }

    @Override
    public Country get(Object key) {
        return null;
    }

    @Override
    public void save(Country object) {

    }

    @Override
    public void remove(Country object) {

    }

    @Override
    public List<Country> all() {
        return null;
    }

}
