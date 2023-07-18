package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Bank {

    private Object who;

    private double balance = 0;
    private BankType type = BankType.GOVERNMENT;

    public Bank(Object who) {
        this.who = who;
    }

    public Bank(Object who, double balance) {
        this.who = who;
        this.balance = balance;
    }

    public Bank(Object who, BankType type) {
        this.who = who;
        this.type = type;
    }

    public Bank(Object who, double balance, BankType type) {
        this.who = who;
        this.balance = balance;
        this.type = type;
    }

    public double current() {
        if (type == BankType.GOVERNMENT) {
            return balance;
        } else {
            return Cities.getInstance().getEconomy().getBalance(Bukkit.getOfflinePlayer(((Citizen) who).getUniqueId()));
        }
    }

    public void deposit(double amount) {
        if (type == BankType.GOVERNMENT) {
            this.balance += amount;
        } else {
            Cities.getInstance().getEconomy().depositPlayer(Bukkit.getOfflinePlayer(((Citizen) who).getUniqueId()), amount);
        }
    }

    public void withdraw(double amount) {
        if (type == BankType.GOVERNMENT) {
            this.balance -= amount;
        } else {
            Cities.getInstance().getEconomy().withdrawPlayer(Bukkit.getOfflinePlayer(((Citizen) who).getUniqueId()), amount);
        }
    }

    public void set(double amount) {
        if (type == BankType.GOVERNMENT) {
            this.balance = amount;
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(((Citizen) who).getUniqueId());
            double bal = Cities.getInstance().getEconomy().getBalance(player);
            if (bal < amount) {
                Cities.getInstance().getEconomy().depositPlayer(player, bal + (amount - bal));
            } else if (bal > amount) {
                Cities.getInstance().getEconomy().depositPlayer(player, bal - (bal - amount));
            }
        }
    }

    public void pay(Bank to, double amount) {
        withdraw(amount);
        to.deposit(amount);
    }

    public void pay(Citizen citizen, double amount) {
        withdraw(amount);
        OfflinePlayer player = Bukkit.getOfflinePlayer(((Citizen) who).getUniqueId());
        Cities.getInstance().getEconomy().depositPlayer(player, amount);
    }

    public void reset() {
        set(0);
    }

}
