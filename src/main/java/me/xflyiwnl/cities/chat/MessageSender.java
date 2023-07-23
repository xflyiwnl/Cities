package me.xflyiwnl.cities.chat;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Translator;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.HashMap;

public class MessageSender {

    private final Citizen citizen;
    private String path;
    private Message message;

    private HashMap<String, String> replace = new HashMap<String, String>();

    public MessageSender(Citizen citizen) {
        this.citizen = citizen;
    }

    public MessageSender path(String path) {
        this.path = path;
        return this;
    }

    public MessageSender replace(String placeholder, String value) {
        replace.put(placeholder, value);
        return this;
    }

    public MessageSender message(String text) {
        message = new Message(text);
        return this;
    }

    public Message run() {
        if (message == null) {
            message = new Message(Translator.of(path));
        }
        replace.forEach((s, s2) -> {
            String replacedText = message.getValue().replace("%" + s + "%", s2);
            message.setValue(replacedText);
        });
        citizen.sendMessage(message.getValue());
        return message;
    }

}
