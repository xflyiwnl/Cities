package me.xflyiwnl.cities;

import me.xflyiwnl.cities.config.YAML;
import org.bukkit.plugin.java.JavaPlugin;

public class FileManager {

    private YAML language;
    private YAML settings;

    private YAML cityOnline;

    public void create() {
        language = new YAML("language.yml");
        settings = new YAML("settings.yml");
        cityOnline = new YAML("gui/online-city.yml");
    }

    public YAML getLanguage() {
        return language;
    }

    public YAML getSettings() {
        return settings;
    }

    public YAML getCityOnline() {
        return cityOnline;
    }
}
