package me.xflyiwnl.cities.database.sql;

import com.wiring.api.WiringAPI;
import com.wiring.api.entity.Column;
import com.wiring.api.entity.ColumnType;
import com.wiring.api.entity.WiringResult;
import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CitizenDAO implements CitiesDAO<Citizen> {

    private final WiringAPI api;
    private String database;

    public CitizenDAO(WiringAPI api, String database) {
        this.api = api;
        this.database = database;

        create();
    }

    @Override
    public void create() {
        api.getDatabase(database)
                .createTable("citizens")
                .column(new Column("uuid", ColumnType.VARCHAR).primaryKey().notNull())
                .column(new Column("name", ColumnType.VARCHAR).notNull())
                .column(new Column("city", ColumnType.VARCHAR))
                .column(new Column("rank", ColumnType.VARCHAR))
                .column(new Column("registered", ColumnType.VARCHAR).notNull())
                .column(new Column("joinedCity", ColumnType.VARCHAR))
                .execute();
    }

    @Override
    public Citizen get(Object key) {
        WiringResult result = api.select(database)
                .table("citizens")
                .value(key)
                .execute();

        return get(result);
    }

    @Override
    public Citizen get(WiringResult result) {
        return new Citizen(
                UUID.fromString(result.get("uuid").toString()), null, null, result.get("registered").toString(), result.get("joinedCity").toString()
        );
    }

    @Override
    public void save(Citizen object) {
        api.insert(database)
                .table("citizens")
                .column("uuid", object.getUniqueId().toString())
                .column("name", object.getName())
                .column("city", object.getCity() == null ? null : object.getCity().getUniqueId().toString())
                .column("rank", object.getRank() == null ? null : object.getRank().getUniqueId().toString())
                .column("registered", object.getRegistered())
                .column("joinedCity", object.getJoinedCity())
                .execute();
    }

    @Override
    public void remove(Citizen object) {
        api.delete(database)
                .table("citizens")
                .value(object.getUniqueId().toString())
                .execute();
    }

    @Override
    public List<Citizen> all() {
        List<Citizen> citizens = new ArrayList<Citizen>();

        List<WiringResult> results = api.selectAll(database)
                .table("citizens")
                .execute();

        for (WiringResult result : results) {
            Citizen citizen = get(result);
            citizens.add(citizen);
        }

        return citizens;
    }
}
