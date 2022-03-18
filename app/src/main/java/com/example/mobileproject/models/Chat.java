package com.example.mobileproject.models;

public class Chat {
    private String msg;
    private String nameUser;
    public Chat(){}

    public Chat(String nameUser,String msg){
        this.nameUser = nameUser;
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public String getNameUser() {
        return nameUser;
    }

}
