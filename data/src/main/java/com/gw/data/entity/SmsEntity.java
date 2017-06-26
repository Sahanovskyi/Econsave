package com.gw.data.entity;


public class SmsEntity {
    // Number from witch the sms was send
    private String mNumber;
    // SMS text body
    private String mBody;

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        this.mNumber = number;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        this.mBody = body;
    }
}
