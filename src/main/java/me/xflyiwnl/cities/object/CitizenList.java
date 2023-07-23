package me.xflyiwnl.cities.object;

import java.util.List;

public interface CitizenList {

    List<Citizen> getCitizens();

    void addCitizen(Citizen citizen);
    void kickCitizen(Citizen citizen1, Citizen citizen2);
    void leaveCitizen(Citizen citizen);

}
