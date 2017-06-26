package com.gw.domain.model;

import com.gw.domain.model.PrivatBank.PrivatBankClient;

public class User {
    private String mName;
    private String mSurname;
    private String mEmail;
    private PrivatBankClient mPrivatBankClient;
    private double mWealth;

    public User(String name, String surname, String email) {
        this.mName = name;
        this.mSurname = surname;
        this.mEmail = email;
    }

    public User(String name, String surname, String email, PrivatBankClient privatBankClient) {
        this.mName = name;
        this.mSurname = surname;
        this.mEmail = email;
        this.mPrivatBankClient = privatBankClient;
    }


    public String getName() {
        return mName;
    }

    public String getSurname() {
        return mSurname;
    }

    public String getEmail() {
        return mEmail;
    }

    public PrivatBankClient getPrivatBankClient() {
        return mPrivatBankClient;
    }


    public void setName(String name) {
        this.mName = name;
    }

    public void setSurname(String surname) {
        this.mSurname = surname;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public void setPrivatBankClient(PrivatBankClient privatBankClient) {
        this.mPrivatBankClient = privatBankClient;
    }

}
