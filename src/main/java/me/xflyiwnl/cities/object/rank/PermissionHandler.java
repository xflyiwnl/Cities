package me.xflyiwnl.cities.object.rank;

import java.util.List;

public interface PermissionHandler {

    List<PermissionNode> getNodes();

    default boolean hasPermission(PermissionNode node) {
        return getNodes().contains(node);
    }

    default void addPermission(PermissionNode node) {
        getNodes().add(node);
    }

    default void removePermission(PermissionNode node) {
        getNodes().remove(node);
    }

}
