package com.example.mobileproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.models.Account;
import com.example.mobileproject.utils.FcmNotificationsSender;
import com.example.mobileproject.utils.FirebaseMessagingService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    Button buttonDraw, buttonWatch, buttonProfile,buttonNotification;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonDraw = (Button) findViewById(R.id.buttonDraw);
        buttonWatch = (Button) findViewById(R.id.buttonWatch);
        buttonProfile = (Button) findViewById(R.id.buttonProfile);
        buttonNotification=(Button)findViewById(R.id.ButtonNotification);


        buttonDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent drawIntent = new Intent(MainActivity.this, DrawActivity.class);
                drawIntent.putExtra("roomID", GlobalConstants.currentRoomID);
                drawIntent.putExtra("vocab", "Hello World");
                startActivity(drawIntent);
            }
        });

        buttonWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent drawIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(drawIntent);
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             mAuth = FirebaseAuth.getInstance();
             FirebaseUser currentUser = mAuth.getCurrentUser();
             System.out.println("CURRENT USER" + currentUser);
             if (currentUser == null) {
                 Toast.makeText(MainActivity.this, "Profile only available for logged user", Toast.LENGTH_SHORT).show();
                 Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
                 startActivity(LoginIntent);
             } else {

                 String accountName = "User01";
                 final Account[] accountList = {new Account("example@gmail.com", "password")};

                 DocumentReference documentReference = Account.getDataFromFirebase(accountName);
                 if (documentReference != null) {
                     documentReference.get().addOnSuccessListener(documentSnapshot -> {
                         accountList[0] = documentSnapshot.toObject(Account.class);
                         Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                         intent.putExtra("account", (Serializable) accountList[0]);
                         startActivity(intent);


                     });
                 }
             }
         }
     });
        buttonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                        FirebaseMessagingService.getToken(getApplicationContext()),
                        "Testing",
                        "How are you",getApplicationContext(), MainActivity.this);
                notificationsSender.SendNotifications();
            }
        });
    }
}