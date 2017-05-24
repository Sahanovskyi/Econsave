package com.gw.data.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Transaction Item Entity used in the data layer.
 */
public class TransactionItemEntity implements Serializable{

    @SerializedName("description")
    private String description;

    @SerializedName("amount")
    private double amount;

    @SerializedName("balance")
    private double balance;

    @SerializedName("date")
    private Date date;

    @SerializedName("currency")
    private String currency;

    public TransactionItemEntity(double amount, double balance, Date date, String description, String currency) {
        this.description = description;
        this.amount = amount;
        this.balance = balance;
        this.date = date;
        this.currency = currency;
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
}
