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
}
