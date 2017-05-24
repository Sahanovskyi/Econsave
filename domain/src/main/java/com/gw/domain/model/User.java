package com.gw.domain.model;

import com.gw.domain.model.PrivatBank.PrivatBankClient;

public class User {
    private String name;
    private String surname;
    private String email;
    private PrivatBankClient privatBankClient;
    private double wealth;

    public User(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public User(String name, String surname, String email, PrivatBankClient privatBankClient) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.privatBankClient = privatBankClient;
    }


    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public PrivatBankClient getPrivatBankClient() {
        return privatBankClient;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPrivatBankClient(PrivatBankClient privatBankClient) {
        this.privatBankClient = privatBankClient;
    }

}
