package com.gw.presentation.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by vadym on 10.05.17.
 */

public class TransactionItemModel  implements Serializable {
    private String description;
    private double amount;
    private double balance;
    private final Date date;
    private String currency;


    public TransactionItemModel(Date date, double amount) {
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

    @Override
    public String toString() {
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        return String.format(Locale.UK, "%.2f,\t %s, %s", amount, dt1.format(date), description);
    }
}
