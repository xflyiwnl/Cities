package me.xflyiwnl.cities;

import me.xflyiwnl.cities.config.YAML;

public class FileManager {

    private YAML language;
    private YAML settings;

    public void create() {
        language = new YAML("language.yml");
        settings = new YAML("settings.yml");

        new YAML("gui/city/online-city.yml");

        new YAML("gui/rank/rank.yml");
        new YAML("gui/rank/rank-edit.yml");
    }

    public YAML get(String path) {
        return new YAML(path);
    }

    public YAML getLanguage() {
        return language;
    }

    public YAML getSettings() {
        return settings;
    }

}
