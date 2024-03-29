package me.xflyiwnl.cities.object.bank.types;

import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Government;
import me.xflyiwnl.cities.object.bank.Bank;
import me.xflyiwnl.cities.object.bank.BankType;

public class GovernmentBank implements Bank {

    private final Government government;
    private double balance = 0;

    public GovernmentBank(Government government) {
        this.government = government;
    }

    public GovernmentBank(Government government, double balance) {
        this.government = government;
        this.balance = balance;
    }

    @Override
    public double current() {
        return balance;
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
    }

    @Override
    public void withdraw(double amount) {
        balance -= amount;
    }

    @Override
    public void set(double amount) {
        balance = amount;
    }

    @Override
    public void pay(Bank to, double amount) {
        withdraw(amount);
        to.deposit(amount);
    }

    @Override
    public void pay(Citizen citizen, double amount) {
        withdraw(amount);
        citizen.getBank().deposit(amount);
    }

    @Override
    public void reset() {
        balance = 0;
    }

    @Override
    public BankType type() {
        return BankType.GOVERNMENT;
    }
}
