package me.xflyiwnl.cities.object;

import java.util.List;

public interface Claimable {

    List<Land> getLands();

    default void claimLand(Land land) {
        getLands().add(land);
    }

    default void unclaimLand(Land land) {
        getLands().remove(land);
    }

}
