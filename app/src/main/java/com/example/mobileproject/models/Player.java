package com.example.mobileproject.models;


import java.io.Serializable;
import java.util.Locale;

public class Player implements Serializable {
    private String name;
    private int point;
    int avatar;
    String token;
    int status; // 0: nothing , 1: RightAnswer , 2: Drawing

    public Player(String name, int point, int avatar) {
        this.name = name;
        this.point = point;
        this.avatar = avatar;

    }

    public Player(String name, int point, int avatar,String token) {
        this.name = name;
        this.point = point;
        this.avatar = avatar;
        this.token=token;
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


    public  String getToken(){return token;}

    public static int checkAnswer(String result, String answer) {
        result = result.toLowerCase();
        answer = answer.toLowerCase();
        System.out.println(result + "---------------------" + answer);
        if (answer.equals(result)) {
            return 2;
        }
        else if(result.contains(answer) && (float)answer.length()/result.length() > 0.7){
            return 1;
        }
        return 0;
    }

    public void getPointGuessPlayer() {
        this.point += 9;
    }

    public void getPointDrawPlayer(Boolean checkFirstAnswerRight) {
        if (checkFirstAnswerRight) {
            this.point += 11;
        } else {
            this.point += 2;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
