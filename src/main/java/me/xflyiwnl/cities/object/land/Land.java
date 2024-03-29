package me.xflyiwnl.cities.object.land;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.object.CitiesObject;
import me.xflyiwnl.cities.object.Saveable;
import me.xflyiwnl.cities.object.WorldCord2;
import me.xflyiwnl.cities.object.city.City;

import java.util.UUID;

public class Land extends CitiesObject implements Saveable {

    private WorldCord2 cord2;

    private LandType type;
    private City city;

    private boolean isSpawnLand = false;

    public Land(WorldCord2 cord2, LandType type, City city) {
        super("");
        this.cord2 = cord2;
        this.type = type;
        this.city = city;
    }

    public Land(UUID uuid, WorldCord2 cord2, LandType type, City city) {
        super(uuid);
        this.cord2 = cord2;
        this.type = type;
        this.city = city;
    }

    public Land(UUID uuid, WorldCord2 cord2, LandType type, City city, boolean isSpawnLand) {
        super(uuid);
        this.cord2 = cord2;
        this.type = type;
        this.city = city;
        this.isSpawnLand = isSpawnLand;
    }

    public boolean connected() {

        WorldCord2 cord = null;
        Land land = null;

        cord = cord2.clone();
        land = CitiesAPI.getInstance().getLandByCord(cord.setZ(cord.getZ() + 1));
        if (land != null && land.getCity().equals(city)) {
            return true;
        }

        cord = cord2.clone();
        land = CitiesAPI.getInstance().getLandByCord(cord.setZ(cord.getZ() - 1));
        if (land != null && land.getCity().equals(city)) {
            return true;
        }

        cord = cord2.clone();
        land = CitiesAPI.getInstance().getLandByCord(cord.setX(cord.getX() + 1));
        if (land != null && land.getCity().equals(city)) {
            return true;
        }

        cord = cord2.clone();
        land = CitiesAPI.getInstance().getLandByCord(cord.setX(cord.getX() - 1));
        if (land != null && land.getCity().equals(city)) {
            return true;
        }

        return false;
    }

    public boolean hasCity() {
        return city != null;
    }

    @Override
    public void create() {
        Cities.getInstance().getLands().put(cord2, this);
    }

    @Override
    public void save() {
    }

    @Override
    public void remove() {
        Cities.getInstance().getLands().remove(this);
    }

    public WorldCord2 getCord2() {
        return cord2;
    }

    public void setCord2(WorldCord2 cord2) {
        this.cord2 = cord2;
    }

    public LandType getType() {
        return type;
    }

    public void setType(LandType type) {
        this.type = type;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public boolean isSpawnLand() {
        return isSpawnLand;
    }

    public void setSpawnLand(boolean spawnLand) {
        isSpawnLand = spawnLand;
    }
}
