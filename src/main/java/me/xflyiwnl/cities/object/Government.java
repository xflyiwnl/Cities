package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.object.bank.GovernmentBank;

import java.util.UUID;

public abstract class Government extends CitiesObject implements BankHandler, Saveable {

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
    public void save() {

    }

    @Override
    public void remove() {

    }

    @Override
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }



}
