package me.xflyiwnl.cities.object.country;

import me.xflyiwnl.cities.object.city.City;

import java.util.Map;
import java.util.UUID;

public interface CitiesList {

    Map<UUID, City> getCities();

    default City getCity(UUID uniqueId) {
        return getCities().get(uniqueId);
    }

    default City getCity(String name) {
        return getCities().values().stream()
                .filter(city -> {
                    return city.getName().equalsIgnoreCase(name);
                })
                .findFirst().orElse(null);
    }

    default void addCity(City city) {
        getCities().put(city.getUniqueId(), city);
    }
    default boolean hasCity(City city) {
        return hasCity(city.getUniqueId());
    }
    default boolean hasCity(UUID uniqueId) {
        return getCities().containsKey(uniqueId);
    }

    default void removeCity(City city) {
        getCities().remove(city.getUniqueId());
    }

}
