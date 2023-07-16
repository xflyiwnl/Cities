package me.xflyiwnl.cities.database.sql;

import me.xflyiwnl.cities.object.CitiesObject;
import me.xflyiwnl.cities.object.Country;

import java.util.UUID;

public interface CitiesDAO<T> {

    void get(T object);
    void save(T object);
    void remove(T object);

    void all(T object);

}
