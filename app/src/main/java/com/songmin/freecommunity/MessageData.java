package com.songmin.freecommunity;

/**
 * Created by 송민 on 2017-04-10.
 */
public class MessageData {
    private String date;
    private String message;
    private String sendNumber;
    private String myNumer;

    public MessageData(String date, String message, String sendNumber, String myNumer) {
        this.date = date;
        this.message = message;
        this.sendNumber = sendNumber;
        this.myNumer = myNumer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(String sendNumber) {
        this.sendNumber = sendNumber;
    }

    public String getMyNumer() {
        return myNumer;
    }

    public void setMyNumer(String myNumer) {
        this.myNumer = myNumer;
    }
}
