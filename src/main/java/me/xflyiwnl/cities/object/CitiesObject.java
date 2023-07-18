package me.xflyiwnl.cities.object;

import java.util.UUID;

public class CitiesObject implements Nameable, Identifyable {

    private String name;
    private UUID uuid = UUID.randomUUID();

    public CitiesObject() {
    }

    public CitiesObject(String name) {
        this.name = name;
    }

    public CitiesObject(UUID uuid) {
        this.uuid = uuid;
    }

    public CitiesObject(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String gotFormattedName() {
        return Nameable.super.gotFormattedName();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    public void setUniqueId(UUID uuid) {
        this.uuid = uuid;
    }

}
