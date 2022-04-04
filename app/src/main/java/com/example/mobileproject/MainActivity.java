package com.example.mobileproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.models.Account;
import com.example.mobileproject.utils.CloudFirestore;
import com.example.mobileproject.utils.FcmNotificationsSender;
import com.example.mobileproject.utils.FriendRequestService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    Button buttonDraw, buttonWatch, buttonProfile,buttonNotification,
    buttonDeleteListRooms, buttonDeleteRoomsState;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonDraw = (Button) findViewById(R.id.buttonDraw);
        buttonWatch = (Button) findViewById(R.id.buttonWatch);
        buttonProfile = (Button) findViewById(R.id.buttonProfile);
        buttonNotification=(Button)findViewById(R.id.ButtonNotification);
        buttonDeleteListRooms=(Button)findViewById(R.id.buttonDeleteListRooms);
        buttonDeleteRoomsState=(Button)findViewById(R.id.buttonDeleteRoomState);


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
             if (currentUser == null) {
                 Toast.makeText(MainActivity.this, "Profile only available for logged user", Toast.LENGTH_SHORT).show();
                 Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
                 startActivity(LoginIntent);
             } else {

                 String accountId = currentUser.getUid();
//                 FirebaseUser acc=Account.getcurrentAccount();
//                 final Account[] accountList = { new Account(acc.getEmail(),acc.getEmail(),"password",acc.getUid())};

                 DocumentReference documentReference = Account.getDataFromFirebase(accountId);
                 if (documentReference != null) {
                     documentReference.get().addOnSuccessListener(documentSnapshot -> {
                         Account acc = documentSnapshot.toObject(Account.class);
                         Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                         intent.putExtra("account", (Serializable) acc);
                         startActivity(intent);


                     });
                 }
             }
         }
     });

        buttonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FriendRequestService.createToken(getApplicationContext());

//                String token= FriendRequestService.getToken(getApplicationContext());
//                FirebaseMessaging.getInstance().getToken()
//                        .addOnCompleteListener(task -> {
//                            if (task.isSuccessful() && task.getResult() != null) {
//                                Log.e("newToken", task.getResult());
//                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
//                                        task.getResult(),
//                                        "Friend request",
//                                        "Accept friend request from User 01",getApplicationContext(), MainActivity.this);
//                                notificationsSender.SendNotifications();
//                            }
//                            else{
//                                Log.e("newToken","empty");
//
//                            }
//                        });
                FriendRequestService.sendMessage(getApplicationContext(),MainActivity.this,"alo","123",null);

            }
        });

        buttonDeleteListRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloudFirestore.deleteAllDocuments("ListofRooms");
            }
        });


        buttonDeleteRoomsState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloudFirestore.deleteAllDocuments("RoomState");
            }
        });

    }
}