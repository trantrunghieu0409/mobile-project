package com.example.mobileproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CloudFirestore {
    public static FirebaseFirestore db;
    static final CloudFirestore INSTANCE = new CloudFirestore();

    private CloudFirestore(){
        db = FirebaseFirestore.getInstance();
    }
    public static CloudFirestore getInstance(){
        return INSTANCE;
    }

    public static Task<Void> sendData(String collection, Object document, Object data) {
        CollectionReference dbCollection = db.collection(collection);
        return dbCollection.document(String.valueOf(document)).set(data);
    }

    public static DocumentReference getData(String collection, String document) {
        try {
            return db.collection(collection).document(document);
        }
        catch (Exception e) {
            Log.e("Get Data From Firebase Error", collection + " - " +
                    document + " not found!");
        }
        return null;
    }

    public static void updateField(String collection, String document, String field, Object value) {
        try {
            db.collection(collection).document(document).update(field, value);
        }
        catch (Exception e) {
            Log.e("Update on Firebase Error", collection + " - " +
                    document + " not found!");
        }
    }

    public static Task<QuerySnapshot> getListofRooms(){
        return db.collection("ListofRooms").get();
    }

    public static void deleteDoc(String collectionName, String document){
        db.collection(collectionName).document(document).delete();
    }

    public static void deleteAllDocuments(String collection) {
        String TAG = "Get Data From Firebase";
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                deleteDoc(collection, document.getId());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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
