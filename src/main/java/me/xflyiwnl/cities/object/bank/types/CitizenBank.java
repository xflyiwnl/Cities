package me.xflyiwnl.cities.object.bank.types;

import me.xflyiwnl.cities.Cities;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.bank.Bank;
import me.xflyiwnl.cities.object.bank.BankType;
import me.xflyiwnl.cities.object.bank.Transaction;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class CitizenBank implements Bank {

    private final Citizen citizen;
    private final List<Transaction> transactions = new ArrayList<>();

    private Economy economy = Cities.getInstance().getEconomy();

    public CitizenBank(Citizen citizen) {
        this.citizen = citizen;
    }

    @Override
    public List<Transaction> transactions() {
        return transactions;
    }

    @Override
    public double current() {
        return economy.getBalance(citizen.getOfflinePlayer());
    }

    @Override
    public void deposit(double amount) {
        economy.depositPlayer(citizen.getOfflinePlayer(), amount);
    }

    @Override
    public void withdraw(double amount) {
        economy.withdrawPlayer(citizen.getOfflinePlayer(), amount);
    }

    @Override
    public void set(double amount) {
        OfflinePlayer player = citizen.getOfflinePlayer();
        double bal = economy.getBalance(player);
        
        // bad method, vault :)
        if (bal < amount) {
            economy.depositPlayer(player, bal + (amount - bal));
        } else if (bal > amount) {
            economy.depositPlayer(player, bal - (bal - amount));
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
        economy.depositPlayer(citizen.getOfflinePlayer(), amount);
    }

    @Override
    public void reset() {
        set(0);
    }

    @Override
    public BankType type() {
        return BankType.CITIZEN;
    }
}
