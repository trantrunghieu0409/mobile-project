package com.example.mobileproject.models;


import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int point;
    int avatar;

    public Player(String name, int point, int avatar) {
        this.name = name;
        this.point = point;
        this.avatar = avatar;
    }
    public Player(){

    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }

}
