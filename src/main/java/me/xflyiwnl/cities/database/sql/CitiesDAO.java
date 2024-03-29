package me.xflyiwnl.cities.database.sql;

import com.wiring.api.entity.WiringResult;

import java.util.List;

public interface CitiesDAO<T> {

    void create();

    T get(Object key);
    T get(WiringResult result);

    void save(T object);
    void remove(T object);

    List<T> all();

}
