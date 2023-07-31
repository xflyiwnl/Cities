package me.xflyiwnl.cities.object;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Rank {

    private UUID uuid = UUID.randomUUID();
    private City city;

    private String title;
    private Map<PermissionNode, Boolean> nodes = new HashMap<PermissionNode, Boolean>();

    public Rank() {
    }

    public Rank(City city, String title) {
        this.city = city;
        this.title = title;
    }

    public Rank(UUID uuid, City city, String title, Map<PermissionNode, Boolean> nodes) {
        this.uuid = uuid;
        this.city = city;
        this.title = title;
        this.nodes = nodes;
    }

    public boolean hasPermission(PermissionNode node) {
        if (!nodes.containsKey(node)) {
            return false;
        }
        return nodes.get(node);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<PermissionNode, Boolean> getNodes() {
        return nodes;
    }

    public void setNodes(Map<PermissionNode, Boolean> nodes) {
        this.nodes = nodes;
    }
}
