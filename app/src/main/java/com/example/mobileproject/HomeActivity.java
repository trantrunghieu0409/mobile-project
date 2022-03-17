package com.example.mobileproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.mobileproject.draw_config.Config;
import com.mikhaellopez.circularimageview.CircularImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends Activity {
    String[] languages = {"Vietnamese", "English"};
    Integer[] avatars = Config.Avatars;

    Button btnPlay;
    Button btnCreateRoom;
    ImageButton btnNext;
    ImageButton btnPrev;
    ImageView btnLogin;
    CircularImageView avatar;
    EditText edtName;

    int pos = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnCreateRoom = (Button) findViewById(R.id.btnCreateRoom);
        Spinner spinner = (Spinner) findViewById(R.id.languages_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        spinner.setAdapter(adapter);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        avatar = (CircularImageView) findViewById(R.id.circle_avatar);
        edtName = (EditText) findViewById(R.id.edtName);
        btnLogin = (ImageView) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to login here
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(++pos >= avatars.length){
                    pos = 0;
                }
                avatar.setImageResource(avatars[pos]);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(--pos < 0){
                    pos = avatars.length -1;
                }
                avatar.setImageResource(avatars[pos]);
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtName.getText().length() == 0){
                    // Pop up Error message
                }
                else {
                    //go to play screen here
                }
            }
        });
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent roomIntent = new Intent(HomeActivity.this, CreateRoomActivity.class);
                roomIntent.putExtra("name", edtName.getText().toString());
                roomIntent.putExtra("avatar", avatars[pos]);
                startActivityForResult(roomIntent, 0);
            }
        });
    }
}
