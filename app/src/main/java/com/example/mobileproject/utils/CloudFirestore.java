package com.example.mobileproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;

public class CloudFirestore {
    public static FirebaseFirestore db;
    static final CloudFirestore INSTANCE = new CloudFirestore();

    private CloudFirestore(){
        db = FirebaseFirestore.getInstance();
    }
    public static CloudFirestore getInstance(){
        return INSTANCE;
    }

    public static Task<Void> sendData(String collectionName, Object document, Object data) {
        CollectionReference dbCollection = db.collection(collectionName);
        return dbCollection.document(String.valueOf(document)).set(data);
    }

    public static DocumentReference getData(String collectionName, String document) {
        try {
            return db.collection(collectionName).document(document);
        }
        catch (Exception e) {
            Log.e("Firebase Error", collectionName + " - " +
                    document + " not found!");
        }

        return null;
    }

    public static Task<Void> updateField(String collection, String document, String field, Object value) {
        return db.collection(collection).document(document).update(field, value);
    }

    public static Task<QuerySnapshot> getListofRooms(){
        return db.collection("ListofRooms").get();
    }
    public static void deleteDoc(String collectionName, String document){
        db.collection(collectionName).document(document).delete();
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
