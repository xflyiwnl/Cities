package me.xflyiwnl.cities.object.tool.confirmation;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.util.Translator;
import me.xflyiwnl.cities.object.tool.Suggestable;
import me.xflyiwnl.cities.object.tool.Timeable;
import me.xflyiwnl.cities.object.tool.Tool;
import me.xflyiwnl.cities.util.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Confirmation implements Tool, Timeable, Suggestable {

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
    }

    @Override
    public void init() {

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

    @Override
    public void remove() {
        citizen.setConfirmation(null);
    }

    @Override
    public void accept() {
        citizen.sendMessage(Translator.of("confirmation.on-accept"));
        accept.run();
        remove();
    }

    @Override
    public void decline() {
        citizen.sendMessage(Translator.of("confirmation.on-decline/"));
        decline.run();
        remove();
    }

    @Override
    public void timeOut() {
        citizen.sendMessage(Translator.of("confirmation.time-out"));
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
