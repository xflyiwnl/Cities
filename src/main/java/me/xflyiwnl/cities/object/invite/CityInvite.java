package me.xflyiwnl.cities.object.invite;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.City;
import me.xflyiwnl.cities.object.Translator;
import me.xflyiwnl.cities.object.timer.InviteTimer;
import me.xflyiwnl.cities.util.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CityInvite implements Invite {

    private City city;
    private Citizen sender;
    private Citizen receiver;

    private InviteTimer timer;

    public CityInvite(City city, Citizen sender, Citizen receiver) {
        this.city = city;
        this.sender = sender;
        this.receiver = receiver;
        this.timer = new InviteTimer(this, 15);

        sendMessages();
    }

    @Override
    public void sendMessages() {

        Translator.send(sender)
                .path("invite.invite-send")
                .replace("city", city.getName())
                .replace("sender", sender.getName())
                .run();

        Translator.send(receiver)
                .path("invite.message")
                .replace("message", Translator.of("invite.invite-messages.city")
                        .replace("%city%", city.getName())
                        .replace("%sender%", sender.getName()))
                .run();

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

        Translator.send(receiver)
                .path("invite.on-accept-receiver")
                .run();

        if (sender.isOnline()) {
            Translator.send(sender)
                    .path("invite.on-accept-sender")
                    .run();
        }

        if (city == null) {
            Translator.send(receiver)
                    .path("city.unknown-city")
                    .run();
            remove();
            return;
        }

        city.addCitizen(receiver);

        remove();

    }

    @Override
    public void decline() {

        Translator.send(receiver)
                .path("invite.on-decline-receiver")
                .run();

        if (sender.isOnline()) {
            Translator.send(sender)
                    .path("invite.on-decline-sender")
                    .run();
        }

        remove();
    }

    @Override
    public void timeOut() {
        Translator.send(receiver)
                .path("invite.time-out")
                .run();
        remove();
    }

    @Override
    public void remove() {
        if (!timer.isCancelled()) {
            timer.cancel();
        }
        receiver.setInvite(null);
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Citizen getSender() {
        return sender;
    }

    public void setSender(Citizen sender) {
        this.sender = sender;
    }

    public Citizen getReceiver() {
        return receiver;
    }

    public void setReceiver(Citizen receiver) {
        this.receiver = receiver;
    }

    public InviteTimer getTimer() {
        return timer;
    }

    public void setTimer(InviteTimer timer) {
        this.timer = timer;
    }

}
