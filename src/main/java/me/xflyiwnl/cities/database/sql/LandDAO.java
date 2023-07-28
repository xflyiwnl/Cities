package me.xflyiwnl.cities.database.sql;

import com.wiring.api.WiringAPI;
import com.wiring.api.entity.Column;
import com.wiring.api.entity.ColumnType;
import com.wiring.api.entity.WiringResult;
import com.zaxxer.hikari.HikariDataSource;
import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.*;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                .column(new Column("cord2", ColumnType.VARCHAR).notNull())
                .column(new Column("type", ColumnType.VARCHAR).notNull())
                .column(new Column("city", ColumnType.VARCHAR))
                .execute();
    }

    @Override
    public Land get(WiringResult result) {
        String[] split = result.get("cord2").toString().split(",");
        WorldCord2 cord2 = new WorldCord2(Bukkit.getWorld(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2]));
        return new Land(
                UUID.fromString(result.get("uuid").toString()),
                cord2,
                LandType.valueOf(result.get("type").toString().toUpperCase()),
                Cities.getInstance().getCity(UUID.fromString(result.get("city").toString())) == null ? null : Cities.getInstance().getCity(UUID.fromString(result.get("city").toString()))
        );
    }

    @Override
    public Land get(Object key) {
        WiringResult result = api.select("cities")
                .table("lands")
                .execute();
        return get(result);
    }

    @Override
    public void save(Land object) {
        api.insert("cities")
                .table("lands")
                .column("uuid", object.getUniqueId().toString())
                .column("cord2", object.getCord2().getWorld().getName() + "," + object.getCord2().getX() + "," + object.getCord2().getZ())
                .column("type", object.getType().toString())
                .column("city", object.getCity() == null ? null : object.getCity().getUniqueId().toString())
                .execute();
    }

    @Override
    public void remove(Land object) {
        api.delete("cities")
                .table("lands")
                .value(object.getUniqueId().toString())
                .execute();
    }

    @Override
    public List<Land> all() {
        List<Land> lands = new ArrayList<Land>();

        List<WiringResult> results = api.selectAll("cities")
                .table("lands")
                .execute();

        for (WiringResult result : results) {
            Land land = get(result);
            lands.add(land);
        }

        return lands;
    }

}
