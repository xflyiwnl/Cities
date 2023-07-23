package me.xflyiwnl.cities.database.sql;

import com.wiring.api.WiringAPI;
import com.wiring.api.entity.Column;
import com.wiring.api.entity.ColumnType;
import com.wiring.api.entity.WiringResult;
import me.xflyiwnl.cities.object.Citizen;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CitizenDAO implements CitiesDAO<Citizen> {

    private final WiringAPI api;

    public CitizenDAO(WiringAPI api) {
        this.api = api;

        create();
    }

    @Override
    public void create() {
        api.getDatabase("cities")
                .createTable("citizens")
                .column(new Column("uuid", ColumnType.VARCHAR).primaryKey().notNull())
                .column(new Column("name", ColumnType.VARCHAR).notNull())
                .column(new Column("city", ColumnType.VARCHAR))
                .execute();
    }

    @Override
    public Citizen get(Object key) {
        WiringResult result = api.select("cities")
                .table("citizens")
                .value(key)
                .execute();

        return get(result);
    }

    @Override
    public Citizen get(WiringResult result) {
        return new Citizen(
                UUID.fromString(result.get("uuid").toString()), null
        );
    }

    @Override
    public void save(Citizen object) {
        api.insert("cities")
                .table("citizens")
                .column("uuid", object.getUniqueId().toString())
                .column("name", object.getName())
                .column("city", object.getCity() == null ? null : object.getCity().getUniqueId().toString())
                .execute();
    }

    @Override
    public void remove(Citizen object) {
        api.delete("cities")
                .table("citizens")
                .value(object.getUniqueId().toString())
                .execute();
    }

    @Override
    public List<Citizen> all() {
        List<Citizen> citizens = new ArrayList<Citizen>();

        List<WiringResult> results = api.selectAll("cities")
                .table("citizens")
                .execute();

        for (WiringResult result : results) {
            Citizen citizen = get(result);
            citizens.add(citizen);
        }

        return citizens;
    }
}
