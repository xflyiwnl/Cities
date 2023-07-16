package me.xflyiwnl.cities.object;

public class Bank {

    private double balance = 0;

    public double current() {
        return balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void set(double amount) {
        this.balance = amount;
    }

    public void pay(Bank to, double amount) {
        withdraw(amount);
        to.deposit(amount);
    }

    public void reset() {
        set(0);
    }

}
