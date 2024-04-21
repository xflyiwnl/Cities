package me.xflyiwnl.cities;

import me.xflyiwnl.cities.config.YAML;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class FileManager {

    private YAML language, settings, store;

    public void generate() {
        language = new YAML("language.yml");
        settings = new YAML("settings.yml");
        store = new YAML("store/store.yml");

        createGuis();
        createRanks();
    }

    public YAML get(String path) {
        return new YAML(path);
    }

    public void createGuis() {
        List<String> guis = Arrays.asList(
                "city/info-screen.yml",
                "city/lands.yml",
                "city/citizens.yml",
                "city/bank.yml",
                "city/bank-history.yml",

                "rank/rank.yml",
                "rank/rank-edit.yml",

                "switch/switch.yml"
        );

        guis.forEach(gui -> get("gui/" + gui));
    }

    public void createRanks() {
        if (hasFolder("rank")) {
            return;
        }

        createFolder("rank");

        List<String> ranks = Arrays.asList(
                "Министр-Экономики.yml"
        );

        ranks.forEach(s -> {
            new YAML("rank/" + s);
        });

    }

    public boolean hasFolder(String folder) {
        File file = new File(Cities.getInstance().getDataFolder(), folder);
        return file.exists();
    }

    public void createFolder(String folder) {
        File file = new File(Cities.getInstance().getDataFolder(), folder);
        if (file.exists())
            return;
        file.mkdir();
    }

    public YAML getLanguage() {
        return language;
    }

    public YAML getSettings() {
        return settings;
    }

    public YAML getStore() {
        return store;
    }
}
