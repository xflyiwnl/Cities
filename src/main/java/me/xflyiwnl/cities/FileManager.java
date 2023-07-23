package me.xflyiwnl.cities;

import me.xflyiwnl.cities.config.YAML;
import org.bukkit.plugin.java.JavaPlugin;

public class FileManager {

    private YAML language;

    public void create() {
        language = new YAML("language.yml");
    }

    public YAML getLanguage() {
        return language;
    }

}
