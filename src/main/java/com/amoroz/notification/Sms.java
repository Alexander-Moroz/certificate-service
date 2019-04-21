package com.amoroz.notification;

import java.io.Serializable;

public class Sms implements Serializable {
    private String phoneNumber;
    private String body;

    public Sms() {
    }

    public Sms(String phoneNumber, String body) {
        this.phoneNumber = phoneNumber;
        this.body = body;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}