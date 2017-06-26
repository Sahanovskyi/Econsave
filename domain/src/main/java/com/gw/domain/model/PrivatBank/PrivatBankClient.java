package com.gw.domain.model.PrivatBank;

public class PrivatBankClient {
    private int mMerchantId;
    private String mCardNumber;
    private String mMerchantPassword;


    public PrivatBankClient(String cardNumber, int merchantId, String merchantPassword) {
        this.mCardNumber = cardNumber;
        this.mMerchantId = merchantId;
        this.mMerchantPassword = merchantPassword;
    }

    public int getMerchantId() {
        return mMerchantId;
    }

    public String getCardNumber() {
        return mCardNumber;
    }

    public String getMerchantPassword() {
        return mMerchantPassword;
    }
}
