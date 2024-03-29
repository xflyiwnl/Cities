package me.xflyiwnl.cities.database;

public interface CitiesDataSource {

    void start();
    void end();

    void saveAll();
    void loadAll();

}
