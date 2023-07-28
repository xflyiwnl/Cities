package me.xflyiwnl.cities.object;

import org.bukkit.World;

public class WorldCord2 {

    private World world;
    private double x, z;

    public WorldCord2(World world, double x, double z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public WorldCord2 clone() {
        WorldCord2 cloned = new WorldCord2(world, x, z);
        return cloned;
    }

    public World getWorld() {
        return world;
    }

    public WorldCord2 setWorld(World world) {
        this.world = world;
        return this;
    }

    public double getX() {
        return x;
    }

    public WorldCord2 setX(double x) {
        this.x = x;
        return this;
    }

    public double getZ() {
        return z;
    }

    public WorldCord2 setZ(double z) {
        this.z = z;
        return this;
    }
}
