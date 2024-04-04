package me.xflyiwnl.cities.util;

import me.xflyiwnl.cities.Cities;

import java.util.List;

public class Translator {

    public static String of(String path) {
        return Cities.getInstance().getFileManager().getLanguage().yaml().getString("language." + path);
    }

    public static List<String> ofList(String path) {
        return Cities.getInstance().getFileManager().getLanguage().yaml().getStringList("language." + path);
    }

}
