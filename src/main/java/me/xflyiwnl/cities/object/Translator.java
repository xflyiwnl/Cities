package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.chat.MessageSender;
import me.xflyiwnl.cities.util.TextUtil;

public class Translator {

    public static String of(String path) {
        return Cities.getInstance().getFileManager().getLanguage().yaml().getString("language." + path);
    }

    public static MessageSender send(Citizen citizen) {
        return new MessageSender(citizen);
    }

}
