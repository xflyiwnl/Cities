package me.xflyiwnl.cities.database;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.City;
import me.xflyiwnl.cities.object.Country;
import me.xflyiwnl.cities.object.Land;

import java.util.UUID;

public interface CitiesDataSource {

    void start();
    void end();

    void saveAll();
    void loadAll();

}
