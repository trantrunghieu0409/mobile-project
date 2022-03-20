package com.example.mobileproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.example.mobileproject.models.Account;
import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    Button buttonDraw, buttonWatch, buttonProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonDraw = (Button) findViewById(R.id.buttonDraw);
        buttonWatch = (Button) findViewById(R.id.buttonWatch);
        buttonProfile = (Button) findViewById(R.id.buttonProfile);

        buttonDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent drawIntent = new Intent(MainActivity.this, DrawActivity.class);
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
                Account account = new Account("User01", 0, R.drawable.avatar_batman, "19127641@student.hcmus.edu.vn", "11");
                Account.sendDataToFirebase(account);

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
        });
    }
}