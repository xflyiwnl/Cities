package me.xflyiwnl.cities.util;

import me.xflyiwnl.cities.Cities;
import java.util.List;

public class Translator {

    public static String ofString(String path) {
        return TextUtil.colorize(Cities.getInstance().getFileManager().getLanguage().yaml()
                .getString("language." + path));
    }

    public static List<String> ofStringList(String path) {
        return TextUtil.colorize(Cities.getInstance().getFileManager().getLanguage().yaml()
                .getStringList("language." + path));
    }

}
