package me.xflyiwnl.cities.timer.timers;

import me.xflyiwnl.cities.CitiesAPI;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.city.City;
import me.xflyiwnl.cities.object.invite.Invite;
import me.xflyiwnl.cities.object.tool.ask.Ask;
import me.xflyiwnl.cities.object.tool.confirmation.Confirmation;
import me.xflyiwnl.cities.timer.CitiesTimer;

public class ActionTimer extends CitiesTimer {

    @Override
    public void run() {
        citizens();
    }

    private final CitiesAPI api = CitiesAPI.getInstance();

    public void citizens() {
        for (Citizen citizen : api.getCitizens().values()) {
            confirm(citizen);
            ask(citizen);
            invite(citizen);
        }
    }

    public void cities() {
        for (City city : api.getCities().values()) {
            invite(city);
        }
    }

    public void confirm(Citizen citizen) {
        if (!citizen.hasConfirmation()) return;

        Confirmation confirmation = citizen.getConfirmation();
        int seconds = confirmation.getSeconds();

        confirmation.setSeconds(seconds - 1);

        if (confirmation.getSeconds() <= 0) {
            confirmation.timeOut();
        }
    }

    public void ask(Citizen citizen) {
        if (!citizen.hasAsk()) return;

        Ask ask = citizen.getAsk();
        int seconds = ask.getSeconds();

        ask.setSeconds(seconds - 1);

        if (ask.getSeconds() <= 0) {
            ask.timeOut();
        }
    }

    public void invite(Citizen citizen) {
        if (!citizen.hasInvite()) return;

        Invite invite = citizen.getInvite();
        int seconds = invite.getSeconds();

        invite.setSeconds(seconds - 1);

        if (invite.getSeconds() <= 0) {
            invite.timeOut();
        }

    }

    public void invite(City city) {
        if (!city.hasInvite()) return;

        Invite invite = city.getInvite();
        int seconds = invite.getSeconds();

        invite.setSeconds(seconds - 1);

        if (invite.getSeconds() <= 0) {
            invite.timeOut();
        }

    }

}
