package me.xflyiwnl.cities.object.confirmation;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Translator;
import me.xflyiwnl.cities.object.timer.ConfirmationTimer;
import me.xflyiwnl.cities.util.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Confirmation {

    private Citizen citizen;
    private String message;

    private Runnable accept;
    private Runnable decline;

    public ConfirmationTimer timer;

    public Confirmation(Citizen citizen, String message, Runnable accept, Runnable decline) {
        this.citizen = citizen;
        this.message = message;
        this.accept = accept;
        this.decline = decline;
        citizen.setConfirmation(this);
        this.timer = new ConfirmationTimer(this, 15);

        sendMessages();
    }

    public void sendMessages() {

        Translator.send(citizen)
                .path("confirmation.message")
                .replace("message", message)
                .run();

        TextComponent accept = new TextComponent();
        accept.setText(TextUtil.colorize(Translator.of("confirmation.accept")));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/confirmation accept"));

        TextComponent decline = new TextComponent();
        decline.setText(TextUtil.colorize(Translator.of("confirmation.decline")));
        decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/confirmation decline"));

        citizen.getPlayer().spigot().sendMessage(accept, decline);

    }

    public void onAccept() {
        Translator.send(citizen)
                .path("confirmation.on-accept")
                .run();
        accept.run();
        remove();
    }

    public void onDecline() {
        Translator.send(citizen)
                .path("confirmation.on-decline")
                .run();
        decline.run();
        remove();
    }

    public void timeOut() {
        Translator.send(citizen)
                .path("confirmation.time-out")
                .run();
        remove();
    }

    public void remove() {
        if (!timer.isCancelled()) {
            timer.cancel();
        }
        citizen.setConfirmation(null);
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Runnable getAccept() {
        return accept;
    }

    public void setAccept(Runnable accept) {
        this.accept = accept;
    }

    public Runnable getDecline() {
        return decline;
    }

    public void setDecline(Runnable decline) {
        this.decline = decline;
    }

    public ConfirmationTimer getTimer() {
        return timer;
    }

    public void setTimer(ConfirmationTimer timer) {
        this.timer = timer;
    }

}
