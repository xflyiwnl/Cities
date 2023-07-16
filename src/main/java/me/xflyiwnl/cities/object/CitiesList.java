package me.xflyiwnl.cities.object;

import java.util.List;

public interface CitiesList {

    List<City> getCities();

    default void addCity(City city) {
        getCities().add(city);
    }

    default void removeCity(City city) {
        getCities().remove(city);
    }

}
