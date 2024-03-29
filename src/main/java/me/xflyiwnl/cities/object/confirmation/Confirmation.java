package me.xflyiwnl.cities.object.confirmation;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Translator;
import me.xflyiwnl.cities.util.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Confirmation {

    private Citizen citizen;
    private String message;

    private Runnable accept;
    private Runnable decline;

    public int seconds = 15;

    public Confirmation(Citizen citizen, String message, Runnable accept, Runnable decline) {
        this.citizen = citizen;
        this.message = message;
        this.accept = accept;
        this.decline = decline;

        citizen.setConfirmation(this);
        sendMessages();
    }

    public void sendMessages() {

        citizen.sendMessage(Translator.of("confirmation.message")
                .replace("%message%", message));

        TextComponent accept = new TextComponent();
        accept.setText(TextUtil.colorize(Translator.of("confirmation.accept")));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/confirmation accept"));

        TextComponent decline = new TextComponent();
        decline.setText(TextUtil.colorize(Translator.of("confirmation.decline")));
        decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/confirmation decline"));

        citizen.getPlayer().spigot().sendMessage(accept, decline);

    }

    public void onAccept() {
        citizen.sendMessage(Translator.of("confirmation.on-accept"));
        accept.run();
        remove();
    }

    public void onDecline() {
        citizen.sendMessage(Translator.of("confirmation.on-decline/"));
        decline.run();
        remove();
    }

    public void timeOut() {
        citizen.sendMessage(Translator.of("confirmation.time-out"));
        remove();
    }

    public void remove() {
        citizen.setConfirmation(null);
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

}
