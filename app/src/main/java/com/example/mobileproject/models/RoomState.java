package com.example.mobileproject.models;

public class RoomState {
    String roomID;
    int timeLeft;
    String imgBitmap;

    public RoomState() {

    }

    public RoomState(String roomID, int timeLeft, String imgBitmap) {
        this.roomID = roomID;
        this.timeLeft = timeLeft;
        this.imgBitmap = imgBitmap;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public String getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(String imgBitmap) {
        this.imgBitmap = imgBitmap;
    }
}
