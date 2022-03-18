package com.example.mobileproject.models;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;


public class Room {
    int maxPoint;
    String topic;
    ArrayList<Player>players;
    int maxPlayer;
    String roomID;


    public Room(String roomID, int maxPoint, int maxPlayer, String topic, ArrayList<Player> players) {
        this.roomID = roomID;
        this.maxPoint = maxPoint;
        this.topic = topic;
        this.players = players;
        this.maxPlayer = maxPlayer;
    }
    public Room(int maxPoint, int maxPlayer, String topic, Player host){
        this.maxPoint = maxPoint;
        this.maxPlayer = maxPlayer;
        this.topic = topic;
        this.players = new ArrayList<>();
        players.add(host);
    }
    public Room(){
    }
    public String autoCreateRoomID(){
        //generate random string
        int lowerLimit = 97;

        // lower limit for LowerCase Letters
        int upperLimit = 122;

        Random random = new Random();

        // Create a StringBuffer to store the result
        StringBuffer r = new StringBuffer(10);

        for (int i = 0; i < 10; i++) {

            // take a random value between 97 and 122
            int nextRandomChar = lowerLimit
                    + (int)(random.nextFloat()
                    * (upperLimit - lowerLimit + 1));

            // append a character at the end of bs
            r.append((char)nextRandomChar);
        }
        // done
        this.roomID = r.toString();
        return this.roomID;
    }

    public void removePlayer(String name){
        for(Player player : players){
            if (player.equals("name")){
                players.remove(player);
                break;
            }
        }
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
    public String getRoomID(){
        return roomID;
    }

    public int getMaxPlayer() {
        return maxPlayer;
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

    public void addPlayer(Player player){
        this.players.add(player);
    }

    @Override
    public String toString() {
        return "Room{" +
                "maxPoint=" + maxPoint +
                ", topic='" + topic + '\'' +
                ", players=" + players +
                ", maxPlayer=" + maxPlayer +
                ", roomID='" + roomID + '\'' +
                '}';
    }
}
