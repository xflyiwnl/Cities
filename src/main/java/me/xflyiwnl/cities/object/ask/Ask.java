package me.xflyiwnl.cities.object.ask;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Translator;
import me.xflyiwnl.cities.object.timer.AskTimer;

public class Ask {

    private Citizen citizen;
    private String message;
    private AskAction<AskMessage> onChat;
    private Runnable onCancel;

    private AskTimer timer;

    public Ask(Citizen citizen, String message, AskAction<AskMessage> onChat, Runnable onCancel) {
        this.citizen = citizen;
        this.message = message;
        this.onChat = onChat;
        this.onCancel = onCancel;

        if (citizen.hasAsk()) {
            Translator.send(citizen)
                    .path("ask.has-ask")
                    .run();
            return;
        }

        timer = new AskTimer(this, 15);

        citizen.setAsk(this);
        send();
    }

    public void onChat(AskMessage ask) {
        if (ask.getMessage().getValue().equalsIgnoreCase("отмена")) {
            cancel();
            return;
        }

        onChat.execute(ask);
        remove();
    }

    public void send() {
        Translator.send(citizen)
                .path("ask.message")
                .replace("message", message)
                .run();
    }

    public void cancel() {
        Translator.send(citizen)
                .path("ask.on-cancel")
                .run();
        onCancel.run();
        remove();
    }

    public void timeOut() {
        Translator.send(citizen)
                .path("ask.time-out")
                .run();
        remove();
    }

    public void remove() {
        citizen.setAsk(null);
        if (!timer.isCancelled()) timer.cancel();
    }

}
