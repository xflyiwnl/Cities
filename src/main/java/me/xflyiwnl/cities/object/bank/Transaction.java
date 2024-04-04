package me.xflyiwnl.cities.object.bank;

import me.xflyiwnl.cities.object.Identifyable;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction implements Identifyable {

    private UUID uniqueId = UUID.randomUUID();

    private String from = "Консоль";
    private double amount = 0;
    private String reason = "Нет причины";

    private TransactionType type;
    private LocalDateTime date = LocalDateTime.now();

    public Transaction() {
    }

    public Transaction(String from, double amount, TransactionType type) {
        this.from = from;
        this.amount = amount;
        this.type = type;
    }

    public Transaction(String from, double amount, String reason, TransactionType type) {
        this.from = from;
        this.amount = amount;
        this.reason = reason;
        this.type = type;
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}
