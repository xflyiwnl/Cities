package me.xflyiwnl.cities.database;

import com.wiring.api.WiringAPI;

public class CitiesDatabase {

    private DatabaseType type;
    private CitiesDataSource source;

    public CitiesDatabase(DatabaseType type) {
        this.type = type;

        connect();
    }

    public void connect() {
        if (type == DatabaseType.SQL) {
            source = new SQLDataSource();
        } else if (type == DatabaseType.YML) {
            source = new YMLDataSource();
        }

        source.start();
    }

    public DatabaseType getType() {
        return type;
    }

    public CitiesDataSource getSource() {
        return source;
    }

}
