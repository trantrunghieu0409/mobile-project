package com.example.mobileproject.models;

import java.util.ArrayList;
import java.util.Random;

public class RoomState {
    String roomID;
    int timeLeft;
    String imgBitmap;
    String vocab; // vocab that user need to draw/guess
    String hint = "";
    boolean isShowHint = false;

    public RoomState() {

    }

    public boolean isShowHint() {
        return isShowHint;
    }

    public void setShowHint(boolean showHint) {
        isShowHint = showHint;
    }

    public RoomState(String roomID, int timeLeft, String imgBitmap, String vocab) {
        this.roomID = roomID;
        this.timeLeft = timeLeft;
        this.imgBitmap = imgBitmap;
        this.vocab = vocab;

        if (vocab != null)
            hint = vocab.replaceAll("\\w+", "-");
    }

    public String getVocab() {
        return vocab;
    }

    public void setVocab(String vocab) {
        this.vocab = vocab;
    }

    public String nextHint() {
        ArrayList<Integer> posArr = new ArrayList<>();
        posArr.add(hint.indexOf('-'));
        while (posArr.get(posArr.size() - 1) != -1) {
            posArr.add(hint.indexOf('-', posArr.get(posArr.size() - 1)));
        }
        if (posArr.size() != 1) { // find at least one '-' character in hint
            Random rnd = new Random();
            int pos = rnd.nextInt(posArr.size() - 1);
            hint = hint.substring(0, pos) + vocab.charAt(pos) + hint.substring(pos + 1);
        }
        return hint;
    }

    public String getHint() {
        return hint;
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
