package me.xflyiwnl.cities.object.invite;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.City;
import me.xflyiwnl.cities.object.Country;
import me.xflyiwnl.cities.object.Translator;
import me.xflyiwnl.cities.object.timer.InviteTimer;
import me.xflyiwnl.cities.util.TextUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CountryInvite implements Invite {

    private Country country;
    private Citizen sender;
    private City receiver;

    private InviteTimer timer;

    public CountryInvite(Country country, Citizen sender, City receiver) {
        this.country = country;
        this.sender = sender;
        this.receiver = receiver;
        this.timer = new InviteTimer(this, 15);

        sendMessages();
    }

    @Override
    public void sendMessages() {

        Translator.send(sender)
                .path("invite.invite-send")
                .run();

        if (receiver.getMayor().isOnline()) {
            Translator.send(receiver.getMayor())
                    .path("invite.message")
                    .replace("message", Translator.of("invite.invite-messages.country")
                            .replace("%country%", country.getName())
                            .replace("%sender%", sender.getName()))
                    .run();

            TextComponent accept = new TextComponent();
            accept.setText(TextUtil.colorize(Translator.of("invite.accept")));
            accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/invite accept"));

            TextComponent decline = new TextComponent();
            decline.setText(TextUtil.colorize(Translator.of("invite.decline")));
            decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/invite decline"));

            receiver.getMayor().getPlayer().spigot().sendMessage(accept, decline);

        }

    }

    @Override
    public void accept() {

        Translator.send(receiver.getMayor())
                .path("invite.on-accept-receiver")
                .run();

        if (sender.isOnline()) {
            Translator.send(sender)
                    .path("invite.on-accept-sender")
                    .run();
        }

        if (country == null) {
            Translator.send(receiver.getMayor())
                    .path("country.unknown-country")
                    .run();
            remove();
            return;
        }

        country.addCity(receiver);

        remove();

    }

    @Override
    public void decline() {

        Translator.send(receiver.getMayor())
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
        if (receiver.getMayor().isOnline()) {
            Translator.send(receiver.getMayor())
                    .path("invite.time-out")
                    .run();
        }
        remove();
    }

    @Override
    public void remove() {
        if (!timer.isCancelled()) {
            timer.cancel();
        }
        receiver.setInvite(null);
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Citizen getSender() {
        return sender;
    }

    public void setSender(Citizen sender) {
        this.sender = sender;
    }

    public City getReceiver() {
        return receiver;
    }

    public void setReceiver(City receiver) {
        this.receiver = receiver;
    }

    public InviteTimer getTimer() {
        return timer;
    }

    public void setTimer(InviteTimer timer) {
        this.timer = timer;
    }

}
