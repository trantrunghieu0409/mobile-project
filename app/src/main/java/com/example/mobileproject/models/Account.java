package com.example.mobileproject.models;

import android.util.Log;

import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;

import kotlin.text.UStringsKt;

public class Account extends Player implements Serializable {
    String email;
    String hashPassword;
    int first_place;
    int second_place;
    int third_place;
    int nGames;
    String acountId;
    ArrayList<String>friendListId;

    public Account() {

    }

    public Account(String name, int point, int avatar, String email, String hashPassword) {
        super(name, point, avatar);
        this.email = email;
        this.hashPassword = hashPassword;
        this.first_place = 0;
        this.second_place = 0;
        this.third_place = 0;
        this.nGames = 0;
    }

    public Account(String email, String hashPassword) {
        this.email = email;
        this.hashPassword = hashPassword;
        this.first_place = 0;
        this.second_place = 0;
        this.third_place = 0;
        this.nGames = 0;
    }

    public Account(String email,String hashPassword,String accountId)
    {
        this.email = email;
        this.hashPassword = null;
        this.first_place = 0;
        this.second_place = 0;
        this.third_place = 0;
        this.nGames = 0;
        this.acountId=accountId;
        friendListId=new ArrayList<String>();
    }

    public int getFirst_place() {
        return first_place;
    }

    public void setFirst_place(int first_place) {
        this.first_place = first_place;
    }

    public int getSecond_place() {
        return second_place;
    }

    public void setSecond_place(int second_place) {
        this.second_place = second_place;
    }

    public int getThird_place() {
        return third_place;
    }

    public void setThird_place(int third_place) {
        this.third_place = third_place;
    }

    public int getnGames() {
        return nGames;
    }

    public void setnGames(int nGames) {
        this.nGames = nGames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public static void sendDataToFirebase(Account account) {
        CloudFirestore.sendData("Account", account.getName(), account).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.e("Firebase Error", "Success");
            }
        });
    }

    public static DocumentReference getDataFromFirebase(String name) {
        DocumentReference documentReference = CloudFirestore.getData("Account", name);
        return documentReference;
    }
}


