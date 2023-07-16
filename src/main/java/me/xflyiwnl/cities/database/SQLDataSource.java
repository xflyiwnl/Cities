package me.xflyiwnl.cities.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.xflyiwnl.cities.database.sql.CitizenDAO;
import me.xflyiwnl.cities.database.sql.CityDAO;
import me.xflyiwnl.cities.database.sql.CountryDAO;
import me.xflyiwnl.cities.database.sql.LandDAO;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.City;
import me.xflyiwnl.cities.object.Country;
import me.xflyiwnl.cities.object.Land;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDataType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class SQLDataSource implements CitiesDataSource {

    private HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;

    private CountryDAO countryDAO;
    private CityDAO cityDAO;
    private CitizenDAO citizenDAO;
    private LandDAO landDAO;

    @Override
    public void start() {

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl( "jdbc:mysql://localhost:3306/database");
        config.setUsername("root");
        config.setPassword("1234");
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );

        dataSource = new HikariDataSource( config );

        try {

            DSLContext dsl = DSL.using(dataSource.getConnection(), SQLDialect.H2);

            dsl.createTableIfNotExists("Country")
                    .column("name", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("uuid", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("bank", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("mayor", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("capital", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("cities", DefaultDataType.getDefaultDataType(SQLDialect.H2, "LONGVARCHAR").notNull().length(5000))
                    .executeAsync();

            dsl.createTableIfNotExists("City")
                    .column("name", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("uuid", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("bank", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("mayor", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("country", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("spawn", DefaultDataType.getDefaultDataType(SQLDialect.H2, "LONGVARCHAR").notNull().length(5000))
                    .column("citizens", DefaultDataType.getDefaultDataType(SQLDialect.H2, "LONGVARCHAR").notNull().length(5000))
                    .column("lands", DefaultDataType.getDefaultDataType(SQLDialect.H2, "LONGVARCHAR").notNull().length(5000))
                    .executeAsync();

            dsl.createTableIfNotExists("Citizen")
                    .column("uuid", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("bank", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("city", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .executeAsync();

            dsl.createTableIfNotExists("Land")
                    .column("name", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("uuid", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("world", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(64))
                    .column("x", DefaultDataType.getDefaultDataType(SQLDialect.H2, "DOUBLE").notNull().length(64))
                    .column("z", DefaultDataType.getDefaultDataType(SQLDialect.H2, "DOUBLE").notNull().length(64))
                    .column("type", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(5000))
                    .column("city", DefaultDataType.getDefaultDataType(SQLDialect.H2, "VARCHAR").notNull().length(5000))
                    .executeAsync();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        countryDAO = new CountryDAO(dataSource);
        cityDAO = new CityDAO(dataSource);
        citizenDAO = new CitizenDAO(dataSource);
        landDAO = new LandDAO(dataSource);

        loadAll();

    }

    @Override
    public void end() {
        saveAll();
        dataSource.close();
    }

    @Override
    public void saveAll() {

    }

    @Override
    public void loadAll() {

    }

    public HikariConfig getConfig() {
        return config;
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    public CountryDAO getCountryDAO() {
        return countryDAO;
    }

    public CityDAO getCityDAO() {
        return cityDAO;
    }

    public CitizenDAO getCitizenDAO() {
        return citizenDAO;
    }

    public LandDAO getLandDAO() {
        return landDAO;
    }

}
