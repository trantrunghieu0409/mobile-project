package com.example.mobileproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.mobileproject.draw_config.Config;
import com.example.mobileproject.models.Player;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.utils.CloudFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

public class CreateRoomActivity extends Activity implements View.OnClickListener {
    String[] numPlayers = {"5 players", "10 players", "15 players", "20 players"};
    String[] maxPoints = {"120 Points", "200 Points", "300 Points", "400 Points"};
    String[] topics = {"Animal", "Household", "Transportation"};
    int[] nPlayers = {5, 10, 15, 20};
    int[] nPoints = {120, 200, 300, 400};
    Integer[] topicImgs = Config.Topics;
    Spinner numPlayerSpinner;
    Spinner maxPointSpinner;
    TextView btnBack;
    TextView txtTopic;
    ImageButton btnNext;
    ImageButton btnPrev;
    Button btnCreate;
    CircularImageView imgTopic;
    int pos = 0;
    String name;

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

        name = getIntent().getStringExtra("name").toString();
        System.out.println(name);
        int avatar = getIntent().getIntExtra("avatar", 0);

        numPlayerSpinner = (Spinner) findViewById(R.id.players_spinner);
        ArrayAdapter<String> playerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, numPlayers);
        numPlayerSpinner.setAdapter(playerAdapter);

        maxPointSpinner = (Spinner) findViewById(R.id.points_spinner);
        ArrayAdapter<String> pointAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, maxPoints);
        maxPointSpinner.setAdapter(pointAdapter);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent playIntent = new Intent(CreateRoomActivity.this, GameplayActivity.class);
                System.out.println(name);
                Player host = new Player(name, 0, avatar);
                Room room = new Room(nPoints[maxPointSpinner.getSelectedItemPosition()], nPlayers[numPlayerSpinner.getSelectedItemPosition()], topics[pos], host);
                room.autoCreateRoomID();
                CloudFirestore.sendData("ListofRooms", room.getRoomID(), room);
                playIntent.putExtra("IDRoom",room.getRoomID());
                startActivity(playIntent);
                finish();
            }
        });

        btnBack = (TextView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
    }

    public void onClick(View view) {
        super.onBackPressed();
    }
}
