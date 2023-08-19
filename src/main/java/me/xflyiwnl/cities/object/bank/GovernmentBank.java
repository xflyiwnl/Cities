package me.xflyiwnl.cities.object.bank;

import me.xflyiwnl.cities.object.Bank;
import me.xflyiwnl.cities.object.Citizen;
import me.xflyiwnl.cities.object.Government;

public class GovernmentBank implements Bank {

    private Government government;
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
        balance -= amount;
        to.deposit(amount);
    }

    @Override
    public void pay(Citizen citizen, double amount) {
        balance -= amount;
        citizen.getBank().deposit(amount);
    }

    @Override
    public void reset() {
        balance = 0;
    }
}
