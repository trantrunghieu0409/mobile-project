package com.example.mobileproject.module;

public class Player {
    private String name;
    private int point;
    public Player(String name, int point){
        this.name = name;
        this.point = point;
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
