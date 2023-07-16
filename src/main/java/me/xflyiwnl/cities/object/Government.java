package me.xflyiwnl.cities.object;

public class Government extends CitiesObject implements BankHandler, Saveable {

    private Bank bank = new Bank();

    public Government(String name) {
        super(name);
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
