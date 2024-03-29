package me.xflyiwnl.cities.object.city;

import me.xflyiwnl.cities.object.land.Land;

public interface Claimable {

    void claimLand(Land land);

    void unclaimLand(Land land);

}
