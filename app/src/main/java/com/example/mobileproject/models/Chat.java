package com.example.mobileproject.models;

import java.util.Random;

public class Chat {
    private String msg;
    private String nameUser = "";
    String timestamp;
    public Chat(){}

    public Chat(String msg){
        this.msg = msg;
        setTimeStampAtTime();
    }

    public Chat(String nameUser,String msg){
        this.nameUser = nameUser;
        this.msg = msg;
        setTimeStampAtTime();
    }

    public String getMsg() {
        return msg;
    }

    public void setTimeStampAtTime(){
        long tsLong = System.currentTimeMillis()/1000;
        this.timestamp = Long.toString(tsLong);
    }

    public String getNameUser() {
        return nameUser;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
