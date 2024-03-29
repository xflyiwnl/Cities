package me.xflyiwnl.cities.database.sql;

import com.wiring.api.WiringAPI;
import com.wiring.api.entity.Column;
import com.wiring.api.entity.ColumnType;
import com.wiring.api.entity.WiringResult;
import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.*;
import me.xflyiwnl.cities.object.city.City;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;

public class CityDAO implements CitiesDAO<City> {

    private WiringAPI api;
    private String database;

    public CityDAO(WiringAPI api, String database) {
        this.api = api;
        this.database = database;

        create();
    }

    @Override
    public void create() {
        if (!api.existsDatabase(database)) {
            return;
        }
        api.getDatabase(database)
                .createTable("cities")
                .column(new Column("uuid", ColumnType.VARCHAR).primaryKey().notNull())
                .column(new Column("name", ColumnType.VARCHAR).notNull())
                .column(new Column("bank", ColumnType.DOUBLE).notNull())
                .column(new Column("mayor", ColumnType.VARCHAR).notNull())
                .column(new Column("country", ColumnType.VARCHAR))
                .column(new Column("spawn", ColumnType.VARCHAR).notNull())
                .column(new Column("board", ColumnType.VARCHAR).notNull())
                .column(new Column("citizens", ColumnType.VARCHAR))
                .execute();
    }

    @Override
    public City get(WiringResult result) {
        String[] formattedLocation = result.get("spawn").toString().split(",");
        Location location = new Location(Bukkit.getWorld(formattedLocation[0]), Double.valueOf(formattedLocation[1]), Double.valueOf(formattedLocation[2]), Double.valueOf(formattedLocation[3]), Float.valueOf(formattedLocation[4]), Float.valueOf(formattedLocation[5]));

        City city = new City(
                result.get("name").toString(),
                UUID.fromString(result.get("uuid").toString()),
                (double) result.get("bank"),
                Cities.getInstance().getCitizen(UUID.fromString(result.get("mayor").toString())),
                null,
                location,
                result.get("board").toString()
        );

        Map<UUID, Citizen> citizens = new HashMap<>();
        for (String formatted : result.get("citizens").toString().split(",")) {
            UUID uuid = UUID.fromString(formatted);
            Citizen citizen = Cities.getInstance().getCitizen(uuid);
            if (citizen == null) continue;
            citizens.put(citizen.getUniqueId(), citizen);
        }

        city.setCitizens(citizens);

        return city;
    }

    @Override
    public City get(Object key) {
        WiringResult result = api.select(database)
                .table("cities")
                .value(key)
                .execute();
        return get(result);
    }

    @Override
    public void save(City object) {
        StringBuilder fc = new StringBuilder();
        object.getCitizens().values().forEach(citizen -> {
            fc.append(citizen.getUniqueId().toString()).append(",");
        });

        api.insert(database)
                .table("cities")
                .column("uuid", object.getUniqueId().toString())
                .column("name", object.getName())
                .column("bank", object.getBank().current())
                .column("mayor", object.getMayor().getUniqueId().toString())
                .column("country", object.getCountry() == null ? null : object.getCountry().getUniqueId().toString())
                .column("spawn",
                        object.getSpawn().getWorld().getName() + ","
                                + object.getSpawn().getX() + ","
                                + object.getSpawn().getY() + ","
                                + object.getSpawn().getZ() + ","
                                + object.getSpawn().getYaw() + ","
                                + object.getSpawn().getPitch()
                        )
                .column("board", object.getBoard())
                .column("citizens", fc.isEmpty() == true ? null : fc.toString())
                .execute();
    }

    @Override
    public void remove(City object) {
        api.delete(database)
                .table("cities")
                .value(object.getUniqueId().toString())
                .execute();
    }

    @Override
    public List<City> all() {
        List<City> cities = new ArrayList<City>();

        List<WiringResult> results = api.selectAll(database)
                .table("cities")
                .execute();

        for (WiringResult result : results) {
            City city = get(result);
            cities.add(city);
        }

        return cities;
    }
}