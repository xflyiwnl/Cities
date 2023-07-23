package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.database.SQLDataSource;
import org.bukkit.World;

import java.util.UUID;

public class Land extends CitiesObject implements Saveable {

    private WorldCord2 cord2;

    private LandType type;
    private City city;

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

    public void create(boolean save) {
        Cities.getInstance().getLands().add(this);
        if (save) {
            save();
        }
    }

    @Override
    public void save() {
        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getLandDAO().save(this);
    }

    @Override
    public void remove() {

        Cities.getInstance().getLands().remove(this);

        SQLDataSource source = (SQLDataSource) Cities.getInstance().getDatabase().getSource();
        source.getLandDAO().remove(this);
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
}
