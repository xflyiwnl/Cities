package me.xflyiwnl.cities.object;

import me.xflyiwnl.cities.object.bank.Bank;
import me.xflyiwnl.cities.object.bank.BankHandler;
import me.xflyiwnl.cities.object.bank.types.GovernmentBank;
import me.xflyiwnl.cities.object.rank.RankHandler;
import me.xflyiwnl.cities.object.rank.Rank;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Government extends CitiesObject implements BankHandler, RankHandler {

    private Bank bank = new GovernmentBank(this);
    private Map<UUID, Rank> ranks = new HashMap<>();

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

    public Rank getRank(UUID uniqueId) {
        return getRanks().get(uniqueId);
    }

    @Deprecated
    public Rank getRankByName(String title) {
        for (Rank rank : ranks.values()) {
            if (rank.getTitle().equalsIgnoreCase(title)) {
                return rank;
            }
        }
        return null;
    }

    @Override
    public Map<UUID, Rank> getRanks() {
        return ranks;
    }

    public void setRanks(Map<UUID, Rank> ranks) {
        this.ranks = ranks;
    }

    @Override
    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

}
