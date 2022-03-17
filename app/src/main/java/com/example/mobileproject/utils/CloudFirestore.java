package com.example.mobileproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mobileproject.models.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CloudFirestore {
    static FirebaseFirestore db;
    static final CloudFirestore INSTANCE = new CloudFirestore();

    private CloudFirestore(){
        db = FirebaseFirestore.getInstance();
    }
    public static CloudFirestore getInstance(){
        return INSTANCE;
    }

    public static String sendData(String collectionName, Object document, Object data) {
        final String[] feedback = {""};
        CollectionReference dbCollection = db.collection(collectionName);

        dbCollection.document(String.valueOf(document)).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        feedback[0] = "Success";
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        feedback[0] = "Fail";
                    }
                });
        return feedback[0];
    }

    public static DocumentReference getData(String collectionName, Object document) {
        try {
            return db.collection(collectionName).document(String.valueOf(document));
        }
        catch (Exception e) {
            System.out.println(collectionName + " - " +
                    String.valueOf(document) + " not found!");
        }

        return null;
    }



    public static String encodeBitmap(Bitmap bmp) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bao); // bmp is bitmap from user image file
        bmp.recycle();
        byte[] byteArray = bao.toByteArray();
        String imageB64 = Base64.encodeToString(byteArray, Base64.URL_SAFE);

        return imageB64;
    }

    public static Bitmap decodeBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.URL_SAFE);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
