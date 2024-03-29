package me.xflyiwnl.cities.object.bank;

import me.xflyiwnl.cities.object.Citizen;

public interface Bank {
    
    double current();
    void deposit(double amount);
    void withdraw(double amount);
    void set(double amount);
    void pay(Bank to, double amount);
    void pay(Citizen citizen, double amount);
    void reset();
    BankType type();

}
