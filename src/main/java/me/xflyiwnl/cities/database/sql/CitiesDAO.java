package me.xflyiwnl.cities.database.sql;

import com.wiring.api.entity.WiringResult;
import me.xflyiwnl.cities.object.CitiesObject;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Country;

import java.util.List;
import java.util.UUID;

public interface CitiesDAO<T> {

    void create();

    T get(Object key);
    T get(WiringResult result);

    void save(T object);
    void remove(T object);

    List<T> all();

}
