package me.xflyiwnl.cities.object.bank;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Bank;
import me.xflyiwnl.cities.object.Citizen;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class CitizenBank implements Bank {

    private Citizen citizen;
    private double balance = 0;

    public CitizenBank(Citizen citizen) {
        this.citizen = citizen;
    }

    public CitizenBank(Citizen citizen, double balance) {
        this.citizen = citizen;
        this.balance = balance;
    }


    @Override
    public double current() {
        return Cities.getInstance().getEconomy().getBalance(Bukkit.getOfflinePlayer((citizen).getUniqueId()));
    }

    @Override
    public void deposit(double amount) {
        Cities.getInstance().getEconomy().depositPlayer(Bukkit.getOfflinePlayer((citizen).getUniqueId()), amount);
    }

    @Override
    public void withdraw(double amount) {
        Cities.getInstance().getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer((citizen).getUniqueId()), amount);
    }

    @Override
    public void set(double amount) {
        OfflinePlayer player = Bukkit.getOfflinePlayer((citizen).getUniqueId());
        double bal = Cities.getInstance().getEconomy().getBalance(player);
        if (bal < amount) {
            Cities.getInstance().getEconomy().depositPlayer(player, bal + (amount - bal));
        } else if (bal > amount) {
            Cities.getInstance().getEconomy().depositPlayer(player, bal - (bal - amount));
        }
    }

    @Override
    public void pay(Bank to, double amount) {
        this.withdraw(amount);
        to.deposit(amount);
    }

    @Override
    public void pay(Citizen citizen, double amount) {
        withdraw(amount);
        OfflinePlayer player = Bukkit.getOfflinePlayer(citizen.getUniqueId());
        Cities.getInstance().getEconomy().depositPlayer(player, amount);
    }

    @Override
    public void reset() {
        set(0);
    }
}
