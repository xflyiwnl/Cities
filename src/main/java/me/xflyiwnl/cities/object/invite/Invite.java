package me.xflyiwnl.cities.object.invite;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Translator;
import me.xflyiwnl.cities.object.timer.ConfirmationTimer;
import me.xflyiwnl.cities.util.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public interface Invite {

    void sendMessages();
    void accept();
    void decline();
    void timeOut();
    void remove();

}
