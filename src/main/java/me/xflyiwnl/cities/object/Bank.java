package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.Cities;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public interface Bank {
    
    double current();
    void deposit(double amount);
    void withdraw(double amount);
    void set(double amount);
    void pay(Bank to, double amount);
    void pay(Citizen citizen, double amount);
    void reset();

}
