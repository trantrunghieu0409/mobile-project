package com.example.mobileproject.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;


public class Room {
    int maxPoint;
    String topic;
    ArrayList<Player>players;
    int maxPlayer;
    String roomID;
    boolean isPlaying = false;
    int drawer = -1;
    int flagCurrentActivity = 0;

    boolean firstAnswer = true;

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
            if (player.getName().equals(name)){
                System.out.println(player.getName());
                players.remove(player);
                break;
            }
        }
    }
    public boolean existedUser(String name){
        for (Player player: players){
            if (player.getName().equals(name)){
                return true;
            }
        }
        return false;
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

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public int getDrawer() {
        return drawer;
    }

    public void setDrawer(int drawer) {
        this.drawer = drawer;
    }

    public int getFlagCurrentActivity() {
        return flagCurrentActivity;
    }

    public void setFlagCurrentActivity(int flagCurrentActivity) {
        this.flagCurrentActivity = flagCurrentActivity;
    }

    public void setFirstAnswer(boolean firstAnswer) {
        this.firstAnswer = firstAnswer;
    }

    public boolean isFirstAnswer() {
        return firstAnswer;
    }

    public void findPlayerAndSetPoint(String name, boolean isDrawer, boolean isFirst){
        for(int i = 0; i < players.size();i++){
            if(players.get(i).getName().equals(name)){
                if(isDrawer){
                    players.get(i).getPointDrawPlayer(isFirst);
                }
                else{
                    players.get(i).getPointGuessPlayer();
                    return;
                }

            }
        }
    }

    public void findPlayerAndSetStatus(String name, int status){
        for(int i = 0; i < players.size();i++){
            if(players.get(i).getName().equals(name)){
                players.get(i).setStatus(status);
            }
        }
    }
    public boolean checkPlayerReachMaxScore(){
        ArrayList<Player> list = sortDescendingPoint();
        if(players.get(0).getPoint() >= maxPoint){
            return true;
        }
        return false;
    }

    public ArrayList<Player> Top3Player(){
        ArrayList<Player> result = new ArrayList<>();
        if(players.size()< 3){
            result.add(sortDescendingPoint().get(0));
        }
        else{
            ArrayList<Player> list = sortDescendingPoint();
            for(int i = 0 ; i < 3 ;i ++){
                result.add(list.get(i));
            }
        }
        return result;
    }

    public void resetAllStatusPlayer(){
        for(int i = 0; i < players.size();i++){
            players.get(i).setStatus(0);
        }
    }


    public ArrayList<Player> sortDescendingPoint() {
        ArrayList<Player> list = this.players;
        Collections.sort(list, new Comparator<Player>() {
            @Override
            public int compare(Player player, Player player2) {
                return Integer.compare(player2.getPoint(), player.getPoint());
            }
        });
        return list;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }
    public String getOwnerUsername(){
        return this.players.get(0).getName();
    }

    public Room deepcopy(){
        Room newRoom = new Room();
        newRoom.setDrawer(this.drawer);
        newRoom.setRoomID(this.roomID);
        newRoom.setPlaying(this.isPlaying);
        newRoom.setPlayers((ArrayList<Player>) this.players.clone());
        newRoom.setTopic(this.topic);
        newRoom.setMaxPlayer(this.maxPlayer);
        newRoom.setMaxPoint(this.maxPoint);
        return newRoom;
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
