package com.example.mobileproject.models;

import java.util.ArrayList;
import com.google.gson.Gson;


public class Room {
    int maxPoint;
    String topic;
    ArrayList<Player>players;

    public Room(int maxPoint, String topic, ArrayList<Player> players) {
        this.maxPoint = maxPoint;
        this.topic = topic;
        this.players = players;
    }

    public int getMaxPoint() {
        return maxPoint;
    }

    public String getTopic() {
        return topic;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setMaxPoint(int maxPoint) {
        this.maxPoint = maxPoint;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(new Room(maxPoint,topic,players));
        return json;

    }
    public Room fromJson(String json){
        Gson gson = new Gson();
        Room room = gson.fromJson(json, Room.class);
        return room;

    }
}
