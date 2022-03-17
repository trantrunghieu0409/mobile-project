package com.example.mobileproject.models;

import com.google.gson.Gson;

public class Player {
    private String name;
    private int point;
    public Player(String name, int point) {
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

    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(new Player(name,point));
        return json;

    }
    public Player fromJson(String json){
        Gson gson = new Gson();
        Player player = gson.fromJson(json, Player.class);
        return player;

    }

}
