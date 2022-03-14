package com.example.mobileproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mikhaellopez.circularimageview.CircularImageView;

public class CreateRoomActivity extends Activity implements View.OnClickListener {
    String[] numPlayers = {"5 players", "10 players", "15 players", "20 players"};
    String[] maxPoints = {"120 Points", "200 Points", "300 Points", "400 Points"};
    String[] topics = {"Animal", "Household", "Transportation"};
    Integer[] topicImgs = {R.drawable.topic1,R.drawable.topic2,R.drawable.topic3};
    Spinner numPlayerSpinner;
    Spinner maxPointSpinner;
    TextView btnBack;
    TextView txtTopic;
    ImageButton btnNext;
    ImageButton btnPrev;
    CircularImageView imgTopic;
    int pos = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createroom);

        imgTopic = (CircularImageView) findViewById(R.id.topic);
        txtTopic = (TextView) findViewById(R.id.txtTopic);

        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(++pos >= topicImgs.length){
                    pos = 0;
                }
                imgTopic.setImageResource(topicImgs[pos]);
                txtTopic.setText(topics[pos]);
            }
        });
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(--pos < 0){
                    pos = topicImgs.length - 1;
                }
                imgTopic.setImageResource(topicImgs[pos]);
            }
        });

        String name = getIntent().getStringExtra("name");
        int avatar = getIntent().getIntExtra("avatar", 0);

        numPlayerSpinner = (Spinner) findViewById(R.id.players_spinner);
        ArrayAdapter<String> playerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, numPlayers);
        numPlayerSpinner.setAdapter(playerAdapter);

        maxPointSpinner = (Spinner) findViewById(R.id.points_spinner);
        ArrayAdapter<String> pointAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, maxPoints);
        maxPointSpinner.setAdapter(pointAdapter);

        btnBack = (TextView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
    }

    public void onClick(View view) {
        super.onBackPressed();
    }
}
