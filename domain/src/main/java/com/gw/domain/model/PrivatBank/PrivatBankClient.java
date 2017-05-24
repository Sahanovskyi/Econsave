package com.gw.domain.model.PrivatBank;

public class PrivatBankClient {
    private int merchantId ;
    private String cardNumber ;
    private String merchantPassword ;


    public PrivatBankClient(String cardNumber, int merchantId, String merchantPassword) {
        this.cardNumber = cardNumber;
        this.merchantId = merchantId;
        this.merchantPassword = merchantPassword;
    }

    public PrivatBankClient(){}

    public int getMerchantId() {
        return merchantId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }
}
