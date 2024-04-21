package me.xflyiwnl.cities.object.switches;

import java.util.List;

public interface SwitchHandler {

    List<SwitchNode> getNodes();

    default boolean hasNode(SwitchNode node) {
        return getNodes().contains(node);
    }

    default void addNode(SwitchNode node) {
        if (!hasNode(node))
            getNodes().add(node);
    }

    default void removeNode(SwitchNode node) {
        if (hasNode(node))
            getNodes().remove(node);
    }

}
