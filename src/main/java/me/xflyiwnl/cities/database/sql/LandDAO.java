package me.xflyiwnl.cities.database.sql;

import com.wiring.api.WiringAPI;
import com.wiring.api.entity.Column;
import com.wiring.api.entity.ColumnType;
import com.wiring.api.entity.WiringResult;
import com.zaxxer.hikari.HikariDataSource;
import me.xflyiwnl.cities.object.CitiesObject;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Land;

import java.util.List;

public class LandDAO implements CitiesDAO<Land> {

    private WiringAPI api;

    public LandDAO(WiringAPI api) {
        this.api = api;

        create();
    }

    @Override
    public void create() {
        api.getDatabase("cities")
                .createTable("lands")
                .column(new Column("uuid", ColumnType.VARCHAR).primaryKey().notNull())
                .column(new Column("world", ColumnType.DOUBLE).notNull())
                .column(new Column("x", ColumnType.DOUBLE).notNull())
                .column(new Column("z", ColumnType.DOUBLE).notNull())
                .column(new Column("type", ColumnType.VARCHAR).notNull())
                .column(new Column("city", ColumnType.VARCHAR))
                .execute();
    }

    @Override
    public Citizen get(WiringResult result) {
        return null;
    }

    @Override
    public Citizen get(Object key) {
        return null;
    }

    @Override
    public void save(Land object) {

    }

    @Override
    public void remove(Land object) {

    }

    @Override
    public List<Land> all() {
        return null;
    }

}
