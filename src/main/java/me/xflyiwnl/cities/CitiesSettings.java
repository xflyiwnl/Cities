package me.xflyiwnl.cities;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class CitiesSettings {

    private FileConfiguration yaml;

    public CitiesSettings(FileConfiguration yaml) {
        this.yaml = yaml;
    }

    public String ofString(String path) {
        return yaml.getString("settings." + path);
    }

    public List<String> ofStringList(String path) {
        return yaml.getStringList("settings." + path);
    }

    public int ofInt(String path) {
        return yaml.getInt("settings." + path);
    }

    public List<Integer> ofIntList(String path) {
        return yaml.getIntegerList("settings." + path);
    }

    public boolean ofBool(String path) {
        return yaml.getBoolean("settings." + path);
    }

    public List<Boolean> ofBoolList(String path) {
        return yaml.getBooleanList("settings." + path);
    }

    public double ofDouble(String path) {
        return yaml.getDouble("settings." + path);
    }

    public List<Double> ofDoubleList(String path) {
        return yaml.getDoubleList("settings." + path);
    }

}
