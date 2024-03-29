package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.object.bank.Bank;
import me.xflyiwnl.cities.object.bank.BankHandler;
import me.xflyiwnl.cities.object.bank.types.GovernmentBank;

import java.util.UUID;

public abstract class Government extends CitiesObject implements BankHandler {

    private Bank bank = new GovernmentBank(this);

    public Government() {
    }

    public Government(String name) {
        super(name);
    }

    public Government(String name, UUID uuid) {
        super(name, uuid);
    }

    public Government(String name, UUID uuid, Bank bank) {
        super(name, uuid);
        this.bank = bank;
    }

    @Override
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

}
