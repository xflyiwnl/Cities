package me.xflyiwnl.cities.object.ask;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Translator;

public class Ask {

    private Citizen citizen;
    private String message;
    private AskAction<AskMessage> onChat;
    private Runnable onCancel;

    private int seconds = 15;

    public Ask(Citizen citizen, String message, AskAction<AskMessage> onChat, Runnable onCancel) {
        this.citizen = citizen;
        this.message = message;
        this.onChat = onChat;
        this.onCancel = onCancel;

        if (citizen.hasAsk()) {
            citizen.sendMessage(Translator.of("ask.has-ask"));
            return;
        }

        citizen.setAsk(this);
        send();
    }

    public void onChat(AskMessage ask) {
        if (ask.getMessage().equalsIgnoreCase("отмена")) {
            cancel();
            return;
        }

        onChat.execute(ask);
        remove();
    }

    public void send() {
        citizen.sendMessage(Translator.of("ask.message")
                .replace("%message%", message));
    }

    public void cancel() {
        citizen.sendMessage(Translator.of("ask.on-cancel"));

        onCancel.run();
        remove();
    }

    public void timeOut() {
        citizen.sendMessage(Translator.of("ask.time-out"));
        remove();
    }

    public void remove() {
        citizen.setAsk(null);
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

}
