package com.gw.domain.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

public class TransactionItem implements Comparable<TransactionItem>, Serializable{
    private String mDescription;
    private double mAmount;
    private double mBalance;
    private final Date mDate;
    private String mCurrency;
    private Category mCategory;


    public TransactionItem(Date date, double amount) {
        this.mAmount = amount;
        this.mDate = date;
    }

    public String getDescription() {
        return mDescription;
    }

    public double getAmount() {
        return mAmount;
    }

    public double getBalance() {
        return mBalance;
    }

    public Date getDate() {
        return mDate;
    }

    public String getCurrency() {
        return mCurrency;
    }

    public Category getCategory() {
        return mCategory;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public void setAmount(double amount) {
        this.mAmount = amount;
    }

    public void setBalance(double balance) {
        this.mBalance = balance;
    }

    public void setCurrency(String currency) {
        this.mCurrency = currency;
    }

    public void setCategory(Category category) {
        this.mCategory = category;
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
