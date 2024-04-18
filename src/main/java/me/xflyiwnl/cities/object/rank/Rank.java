package me.xflyiwnl.cities.object.rank;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.citizen.Citizen;
import me.xflyiwnl.cities.object.Government;
import me.xflyiwnl.cities.object.Identifyable;
import me.xflyiwnl.cities.object.Saveable;
import me.xflyiwnl.cities.object.citizen.CitizenList;

import java.util.*;

public class Rank implements Identifyable, Saveable, PermissionHandler, CitizenList {

    private UUID uniqueId = UUID.randomUUID();
    private Government government;

    private String title;
    private PermissionType type;

    private List<PermissionNode> nodes = new ArrayList<PermissionNode>();
    private final Map<UUID, Citizen> citizens = new HashMap<>();

    public Rank() {
    }

    public Rank(Government government, String title, PermissionType type) {
        this.government = government;
        this.title = title;
        this.type = type;
    }

    public Rank(Government government, String title, PermissionType type, List<PermissionNode> nodes) {
        this.government = government;
        this.title = title;
        this.type = type;
        this.nodes = nodes;
    }

    public Rank(String title, PermissionType type, List<PermissionNode> nodes) {
        this.title = title;
        this.type = type;
        this.nodes = nodes;
    }

    @Override
    public void create() {
        Cities.getInstance().getRanks().put(getUniqueId(), this);
        government.getRanks().put(getUniqueId(), this);
    }

    @Override
    public void save() {
    }

    @Override
    public void remove() {
        Cities.getInstance().getRanks().remove(getUniqueId());
        government.getRanks().remove(getUniqueId());
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public Map<UUID, Citizen> getCitizens() {
        return citizens;
    }

    @Override
    public List<PermissionNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<PermissionNode> nodes) {
        this.nodes = nodes;
    }

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Government getGovernment() {
        return government;
    }

    public void setGovernment(Government government) {
        this.government = government;
    }


}
