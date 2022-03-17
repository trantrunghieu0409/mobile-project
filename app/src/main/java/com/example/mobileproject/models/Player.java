package com.example.mobileproject.models;


public class Player {
    private String name;
    private int point;
    int avatar;
    public Player(String name, int point, int avatar) {
        this.name = name;
        this.point = point;
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
