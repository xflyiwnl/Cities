package me.xflyiwnl.cities.object;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public class WorldCord2 {

    private World world;
    private double x, z;

    public WorldCord2(Location location) {
        this.world = location.getWorld();
        this.x = location.getX();
        this.z = location.getZ();
    }

    public WorldCord2(Chunk chunk) {
        this.world = chunk.getWorld();
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    public WorldCord2(World world, double x, double z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public Chunk getChunk() {
        return new Location(world, x, 0, z).getChunk();
    }

    @Override
    public String toString() {
        return "WorldCord2{" +
                "world=" + world +
                ", x=" + x +
                ", z=" + z +
                '}';
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
