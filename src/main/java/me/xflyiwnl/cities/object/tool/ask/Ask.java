package me.xflyiwnl.cities.object.tool.ask;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Translator;
import me.xflyiwnl.cities.object.tool.Suggestable;
import me.xflyiwnl.cities.object.tool.Timeable;
import me.xflyiwnl.cities.object.tool.Tool;

public class Ask implements Tool, Timeable, Suggestable {

    private Citizen citizen;
    private String message;
    private AskAction<AskMessage> onChat;
    private Runnable onCancel;
    private AskMessage result;

    private int seconds = 15;

    public Ask(Citizen citizen, String message, AskAction<AskMessage> onChat, Runnable onCancel) {
        this.citizen = citizen;
        this.message = message;
        this.onChat = onChat;
        this.onCancel = onCancel;
    }

    public void onChat(AskMessage ask) {
        if (ask.getMessage().equalsIgnoreCase("отмена")) {
            decline();
            return;
        }

        result = ask;
        accept();
    }

    @Override
    public void init() {
        citizen.sendMessage(Translator.of("ask.message")
                .replace("%message%", message));
    }

    @Override
    public void remove() {
        citizen.setAsk(null);
    }

    @Override
    public void accept() {
        onChat.execute(result);
        remove();
    }

    @Override
    public void decline() {
        citizen.sendMessage(Translator.of("ask.on-cancel"));

        onCancel.run();
        remove();
    }

    @Override
    public void timeOut() {
        citizen.sendMessage(Translator.of("ask.time-out"));
        remove();
    }

    @Override
    public int getSeconds() {
        return seconds;
    }

    @Override
    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

}
