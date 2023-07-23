package me.xflyiwnl.cities.object.timer;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.confirmation.Confirmation;
import me.xflyiwnl.cities.object.invite.Invite;
import org.bukkit.scheduler.BukkitRunnable;

public class InviteTimer extends BukkitRunnable {

    private Invite invite;

    public InviteTimer(Invite invite, long time) {
        this.invite = invite;
        this.runTaskLater(Cities.getInstance(), time * 20);
    }

    @Override
    public void run() {
        invite.timeOut();
    }

}