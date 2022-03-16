package com.example.mobileproject.utils;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CloudFirestore {
    FirebaseFirestore db;
    static final CloudFirestore INSTANCE = new CloudFirestore();

    private CloudFirestore(){
        db = FirebaseFirestore.getInstance();
    }
    public static CloudFirestore getInstance(){
        return INSTANCE;
    }
    public String createRoom(String userID){
        // generate RoomID
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        // init point for the host
        Map<String, Object> initPoint = new HashMap<>();
        initPoint.put("playerScore", 0);
        //add to firestore
        db.collection("ListofRooms").document(generatedString)
                .collection("Players").document(userID).set(initPoint).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("createRoom", "Room ID: " + generatedString);
                    }
                }
        );
        //return room ID
        return generatedString;
    }
}
