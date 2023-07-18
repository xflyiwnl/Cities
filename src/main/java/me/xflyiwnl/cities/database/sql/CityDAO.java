package me.xflyiwnl.cities.database.sql;

import com.wiring.api.WiringAPI;
import com.wiring.api.entity.Column;
import com.wiring.api.entity.ColumnType;
import com.wiring.api.entity.WiringResult;
import com.zaxxer.hikari.HikariDataSource;
import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Bank;
import me.xflyiwnl.cities.object.CitiesObject;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.City;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CityDAO implements CitiesDAO<City> {

    private WiringAPI api;

    public CityDAO(WiringAPI api) {
        this.api = api;

        create();
    }

    @Override
    public void create() {
        if (!api.existsDatabase("cities")) {
            return;
        }
        api.getDatabase("cities")
                .createTable("cities")
                .column(new Column("uuid", ColumnType.VARCHAR).primaryKey().notNull())
                .column(new Column("name", ColumnType.VARCHAR).notNull())
                .column(new Column("bank", ColumnType.DOUBLE).notNull())
                .column(new Column("mayor", ColumnType.VARCHAR).notNull())
                .column(new Column("country", ColumnType.VARCHAR))
                .column(new Column("spawn", ColumnType.VARCHAR).notNull())
                .column(new Column("citizens", ColumnType.VARCHAR))
                .column(new Column("lands", ColumnType.VARCHAR))
                .execute();
    }

    @Override
    public Citizen get(WiringResult result) {
        return null;
    }

    @Override
    public Citizen get(Object key) {
        WiringResult result = api.select("cities")
                .table("cities")
                .value(key)
                .execute();

        String[] formattedLocation = result.get("spawn").toString().split(",");
        Location location = new Location(Bukkit.getWorld(formattedLocation[0]), Double.valueOf(formattedLocation[1]), Double.valueOf(formattedLocation[2]), Double.valueOf(formattedLocation[3]), Float.valueOf(formattedLocation[4]), Float.valueOf(formattedLocation[5]));

        City city = new City(
                result.get("name").toString(),
                UUID.fromString(result.get("uuid").toString()),
                (double) result.get("bank"),
                Cities.getInstance().getCitizen(UUID.fromString(result.get("mayor").toString())),
                null,
                location

        );

        List<Citizen> citizens = new ArrayList<Citizen>();
        for (String uid : (List<String>) result.get("citizens")) {
            // todo
        }

        return city;
    }

    @Override
    public void save(City object) {
        List<String> formattedCitizens = new ArrayList<String>();
        object.getCitizens().forEach(citizen -> {
            formattedCitizens.add(citizen.getName());
        });
        List<String> formattedLands = new ArrayList<String>();
        object.getLands().forEach(land -> {
            formattedLands.add(land.getUniqueId().toString());
        });

        api.insert("cities")
                .table("cities")
                .column("uuid", object.getUniqueId().toString())
                .column("name", object.getName())
                .column("bank", object.getBank().current())
                .column("mayor", object.getMayor().getUniqueId().toString())
                .column("country", object.getCountry() == null ? null : object.getCountry().getUniqueId().toString())
                .column("spawn",
                        object.getSpawn().getWorld().toString() + ","
                                + object.getSpawn().getX() + ","
                                + object.getSpawn().getY() + ","
                                + object.getSpawn().getZ() + ","
                                + object.getSpawn().getYaw() + ","
                                + object.getSpawn().getPitch()
                        )
                .column("citizens", formattedCitizens.isEmpty() ? null : formattedCitizens)
                .column("lands", formattedLands.isEmpty() ? null : formattedLands)
                .execute();
    }

    @Override
    public void remove(City object) {
        api.delete("cities")
                .table("cities")
                .value(object.getUniqueId().toString())
                .execute();
    }

    @Override
    public List<City> all() {
        return null;
    }
}