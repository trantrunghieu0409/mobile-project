package com.example.mobileproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button buttonDraw, buttonWatch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonDraw = (Button) findViewById(R.id.buttonDraw);
        buttonWatch = (Button) findViewById(R.id.buttonWatch);

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
    }
}