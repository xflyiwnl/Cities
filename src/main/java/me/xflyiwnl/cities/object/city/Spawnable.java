package me.xflyiwnl.cities.object.city;

import me.xflyiwnl.cities.object.land.Land;
import org.bukkit.Location;

public interface Spawnable {

    Land getSpawnLand();
    Location getSpawn();

}
