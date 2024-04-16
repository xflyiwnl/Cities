package me.xflyiwnl.cities.object.city;

import me.xflyiwnl.cities.object.rank.PermissionNode;
import me.xflyiwnl.cities.object.rank.Rank;

import java.util.Map;
import java.util.UUID;

public interface RankHandler {

    Map<UUID, Rank> getRanks();

    default void addRank(Rank rank) {
        getRanks().put(rank.getUniqueId(), rank);
    }

    default void removeRank(Rank rank) {
        getRanks().remove(rank.getUniqueId());
    }

    default boolean hasRank(Rank rank) {
        return getRanks().containsKey(rank.getUniqueId());
    }

}
