package me.xflyiwnl.cities;

import me.xflyiwnl.cities.config.YAML;

import java.util.Arrays;
import java.util.List;

public class FileManager {

    private YAML language;
    private YAML settings;

    public void generate() {
        language = new YAML("language.yml");
        settings = new YAML("settings.yml");

        createGuis();
    }

    public YAML get(String path) {
        return new YAML(path);
    }

    public void createGuis() {
        List<String> guis = Arrays.asList(
                "city/citizens.yml",
                "city/bank-history.yml",
                "rank/rank.yml",
                "rank/rank-edit.yml"
        );
        guis.forEach(gui -> get("gui/" + gui));
    }

    public YAML getLanguage() {
        return language;
    }

    public YAML getSettings() {
        return settings;
    }

}
