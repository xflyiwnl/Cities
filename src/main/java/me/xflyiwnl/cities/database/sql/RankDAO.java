package me.xflyiwnl.cities.database.sql;

import com.wiring.api.WiringAPI;
import com.wiring.api.entity.Column;
import com.wiring.api.entity.ColumnType;
import com.wiring.api.entity.WiringResult;
import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.PermissionNode;
import me.xflyiwnl.cities.object.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RankDAO implements CitiesDAO<Rank> {

    private WiringAPI api;
    private String database;

    public RankDAO(WiringAPI api, String database) {
        this.api = api;
        this.database = database;

        create();
    }

    @Override
    public void create() {
        api.getDatabase(database)
                .createTable("ranks")
                .column(new Column("uuid", ColumnType.VARCHAR).primaryKey().notNull())
                .column(new Column("government", ColumnType.VARCHAR).notNull())
                .column(new Column("title", ColumnType.VARCHAR).notNull())
                .column(new Column("nodes", ColumnType.VARCHAR))
                .column(new Column("citizens", ColumnType.VARCHAR))
                .execute();
    }

    @Override
    public Rank get(Object key) {
        WiringResult result = api.select(database)
                .table("ranks")
                .value(key)
                .execute();
        return get(result);
    }

    @Override
    public Rank get(WiringResult result) {
        List<PermissionNode> nodes = new ArrayList<PermissionNode>();
        if (!result.get("nodes").equals("null")) {
            for (String formattedNode : result.get("nodes").toString().split(",")) {
                PermissionNode node = PermissionNode.valueOf(formattedNode.toUpperCase());
                if (node == null) {
                    continue;
                }
                nodes.add(node);
            }
        }
        List<Citizen> citizens = new ArrayList<Citizen>();
        if (!result.get("citizens").equals("null")) {
            for (String formatted : result.get("citizens").toString().split(",")) {
                citizens.add(Cities.getInstance().getCitizen(UUID.fromString(formatted)));
            }
        }
        return new Rank(
                UUID.fromString(result.get("uuid").toString()),
                Cities.getInstance().getGovernment(UUID.fromString(result.get("government").toString())),
                result.get("title").toString(),
                nodes,
                citizens
        );
    }

    @Override
    public void save(Rank object) {
        StringBuilder nodes = new StringBuilder();
        object.getNodes().forEach(permissionNode -> {
            nodes.append(permissionNode.toString()).append(",");
        });
        StringBuilder citizens = new StringBuilder();
        object.getCitizens().forEach(citizen -> {
            citizens.append(citizen.getUniqueId()).append(",");
        });
        api.insert(database)
                .table("ranks")
                .column("uuid", object.getUniqueId().toString())
                .column("government", object.getGovernment().getUniqueId().toString())
                .column("title", object.getTitle())
                .column("nodes", nodes.isEmpty() ? null : nodes.toString())
                .column("citizens", citizens.isEmpty() ? null : citizens.toString())
                .execute();
    }

    @Override
    public void remove(Rank object) {
        api.delete(database)
                .table("ranks")
                .value(object.getUniqueId().toString())
                .execute();
    }

    @Override
    public List<Rank> all() {
        List<Rank> ranks = new ArrayList<Rank>();
        List<WiringResult> results = api.selectAll(database)
                .table("ranks")
                .execute();
        results.forEach(result -> {
            ranks.add(get(result));
        });
        return ranks;
    }
}
