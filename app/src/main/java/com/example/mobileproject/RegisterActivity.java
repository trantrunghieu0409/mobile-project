package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    Button registerBtn;
    EditText registerEmail,registerPassword,registerRetypePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerEmail=(EditText) findViewById(R.id.editTextRegsiterEmail);
        registerPassword=(EditText) findViewById(R.id.editTextRegsiterPassword);
        registerRetypePassword=(EditText) findViewById((R.id.editTextRegsiterPasswordRetype));
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}