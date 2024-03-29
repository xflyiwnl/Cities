package me.xflyiwnl.cities.object;

import org.bukkit.World;

import java.util.Objects;

public class WorldCord2 {

    private World world;
    private double x, z;

    public WorldCord2(World world, double x, double z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldCord2 that = (WorldCord2) o;
        return Double.compare(x, that.x) == 0 && Double.compare(z, that.z) == 0 && Objects.equals(world, that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, z);
    }

    @Override
    public String toString() {
        return world.getName() + "," + x + "," + z;
    }

    public WorldCord2 clone() {
        return new WorldCord2(world, x, z);
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
