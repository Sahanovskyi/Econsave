package com.gw.domain.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

public class TransactionItem implements Comparable<TransactionItem>, Serializable{
    private String description;
    private double amount;
    private double balance;
    private final Date date;
    private String currency;
    private Category category;


    public TransactionItem(Date date, double amount) {
        this.amount = amount;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalance() {
        return balance;
    }

    public Date getDate() {
        return date;
    }

    public String getCurrency() {
        return currency;
    }

    public Category getCategory() {
        return category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    @Override
    public int compareTo(@NotNull TransactionItem transactionItem) {
        return getDate().compareTo(transactionItem.getDate());
    }

    public enum Category {
        FOOD,
        ENTERTAINMENT,
        HOME,
        CLOTHES,
        ELECTRONICS,
        HEALTH,
        WORK,
        EDUCATION,
        TRAVEL,
        UTILITIES
    }
}
