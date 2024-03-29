package me.xflyiwnl.cities.object.invite.types;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.country.Country;
import me.xflyiwnl.cities.object.Translator;
import me.xflyiwnl.cities.object.invite.Invite;
import me.xflyiwnl.cities.object.invite.InviteType;
import me.xflyiwnl.cities.timer.InviteTimer;
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

        init();
    }

    @Override
    public void init() {

        sender.sendMessage(Translator.of("invite.invite-send"));
        
        if (receiver.getMayor().isOnline()) {
            receiver.getMayor().sendMessage(
                    Translator.of("invite.message")
                    .replace("message", Translator.of("invite.invite-messages.country")
                            .replace("%country%", country.getName())
                            .replace("%sender%", sender.getName())));

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

        receiver.getMayor().sendMessage(Translator.of("invite.on-accept-receiver"));

        if (sender.isOnline()) {
            sender.sendMessage(Translator.of("invite.on-accept-sender"));
        }

        if (country == null) {
            receiver.getMayor().sendMessage(Translator.of("country.unknown-country"));
            remove();
            return;
        }

        country.addCity(receiver);

        remove();

    }

    @Override
    public void decline() {

        receiver.getMayor().sendMessage(Translator.of("invite.on-decline-receiver"));

        if (sender.isOnline()) {
            sender.sendMessage(Translator.of("invite.on-decline-sender"));
        }

        remove();
    }

    @Override
    public void timeOut() {
        if (receiver.getMayor().isOnline()) {
            receiver.getMayor().sendMessage(Translator.of("invite.time-out"));
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

    @Override
    public InviteType type() {
        return InviteType.COUNTRY;
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
