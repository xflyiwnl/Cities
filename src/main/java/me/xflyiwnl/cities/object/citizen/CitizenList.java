package me.xflyiwnl.cities.object.citizen;

import me.xflyiwnl.cities.object.citizen.Citizen;

import java.util.Map;
import java.util.UUID;

public interface CitizenList {

    Map<UUID, Citizen> getCitizens();

    default void addCitizen(Citizen citizen) {
        getCitizens().put(citizen.getUniqueId(), citizen);
    }
    default void removeCitizen(Citizen citizen) {
        getCitizens().remove(citizen.getUniqueId());
    }

}
