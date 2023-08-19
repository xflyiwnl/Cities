package me.xflyiwnl.cities.object.ask;

import me.xflyiwnl.cities.chat.Message;

public class AskMessage {

    private Message message;

    public AskMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
