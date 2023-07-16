package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.database.SQLDataSource;
import org.bukkit.World;

public class Land extends CitiesObject implements Saveable {

    private World world;
    private double x, z;

    private LandType type;
    private City city;

    public Land(World world, double x, double z, LandType type, City city) {
        super("");
        this.world = world;
        this.x = x;
        this.z = z;
        this.type = type;
        this.city = city;
    }

    @Override
    public void save() {
        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getLandDAO().save(this);
    }

    @Override
    public void remove() {
        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getLandDAO().remove(this);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
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
}
