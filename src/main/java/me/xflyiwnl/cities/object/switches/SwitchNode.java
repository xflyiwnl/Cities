package me.xflyiwnl.cities.object.switches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SwitchNode {

    ENEMY_MOB(SwitchType.CITY, "Монстры"),
    BURNING(SwitchType.CITY, "Огонь"),
    EXPLODE(SwitchType.CITY, "Взрыв"),
    PVP(SwitchType.CITY, "Пвп");

    private SwitchType type;
    private String lore;

    SwitchNode(SwitchType type, String lore) {
        this.type = type;
        this.lore = lore;
    }

    public static SwitchNode getNode(String formatted) {
        for (SwitchNode node : SwitchNode.values()) {
            if (node.toString().equalsIgnoreCase(formatted.toLowerCase())) {
                return node;
            }
        }
        return null;
    }

    public static List<SwitchNode> getNodes(SwitchType type) {
        return Arrays.stream(SwitchNode.values())
                .filter(switchNode -> switchNode.getType() == type)
                .collect(Collectors.toList());
    }

    public SwitchType getType() {
        return type;
    }

    public String getLore() {
        return lore;
    }
}
