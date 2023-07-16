package me.xflyiwnl.cities.object;

import java.util.List;

public interface CitizenList {

    List<Citizen> getCitizens();

    default void addCitizen(Citizen citizen) {
        getCitizens().add(citizen);
    }

    default void removeCitizen(Citizen citizen) {
        getCitizens().remove(citizen);
    }

}
