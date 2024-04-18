package me.xflyiwnl.cities.object.invite.types;

import me.xflyiwnl.cities.object.citizen.Citizen;
import me.xflyiwnl.cities.util.Translator;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.invite.Invite;
import me.xflyiwnl.cities.object.invite.InviteType;
import me.xflyiwnl.cities.util.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CityInvite implements Invite {

    private final City city;
    private final Citizen sender;
    private final Citizen receiver;

    private int seconds = 15;

    public CityInvite(City city, Citizen sender, Citizen receiver) {
        this.city = city;
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public void init() {

        city.broadcast(Translator.of("city.citizen-invited")
                .replace("%citizen%", receiver.getName())
                .replace("%sender%", sender.getName()), true);

        sender.sendMessage(Translator.of("invite.invite-send")
                .replace("%city%", city.getName())
                .replace("%sender%", sender.getName()));

        receiver.sendMessage(Translator.of("invite.message")
                .replace("%message%", Translator.of("invite.invite-messages.city")
                        .replace("%city%", city.getName())
                        .replace("%sender%", sender.getName())));

        TextComponent accept = new TextComponent();
        accept.setText(TextUtil.colorize(Translator.of("invite.accept")));
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/invite accept"));

        TextComponent decline = new TextComponent();
        decline.setText(TextUtil.colorize(Translator.of("invite.decline")));
        decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/invite decline"));

        receiver.getPlayer().spigot().sendMessage(accept, decline);

    }

    @Override
    public void accept() {

        city.broadcast(Translator.of("city.citizen-added")
                .replace("%citizen%", receiver.getName()), true);

        receiver.sendMessage(Translator.of("invite.on-accept-receiver"));

        if (sender.isOnline()) {
            sender.sendMessage(Translator.of("invite.on-accept-sender"));
        }

        if (city == null) {
            receiver.sendMessage(Translator.of("city.unknown-city"));
            remove();
            return;
        }

        city.addCitizen(receiver);

        remove();

    }

    @Override
    public void decline() {

        receiver.sendMessage(Translator.of("invite.on-decline-receiver"));

        if (sender.isOnline()) {
            sender.sendMessage(Translator.of("invite.on-decline-sender"));
        }

        remove();
    }

    @Override
    public void timeOut() {
        receiver.sendMessage(Translator.of("invite.time-out"));
        remove();
    }

    @Override
    public void remove() {
        receiver.setInvite(null);
    }

    @Override
    public InviteType type() {
        return InviteType.CITY;
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
