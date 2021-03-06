package com.example.mobileproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.draw_config.Config;
import com.example.mobileproject.models.Account;
import com.example.mobileproject.models.Player;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.utils.CloudFirestore;
import com.example.mobileproject.utils.FriendRequestService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.Serializable;

public class CreateRoomActivity extends Activity implements View.OnClickListener {
    String[] numPlayers = {"5 players", "10 players", "15 players", "20 players"};
    String[] maxPoints = {"50 Points" ,"120 Points", "200 Points", "300 Points", "400 Points"};

    Integer[] topicImgs = Config.Topics;
    Spinner numPlayerSpinner;
    Spinner maxPointSpinner;
    TextView btnBack;
    TextView txtTopic;
    AppCompatButton btnNext;
    AppCompatButton btnPrev;
    Button btnCreate;
    CircularImageView imgTopic;
    int pos = 0;
    String name;
    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createroom);


        imgTopic = (CircularImageView) findViewById(R.id.topic);
        txtTopic = (TextView) findViewById(R.id.txtTopic);

        btnNext = (AppCompatButton) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(++pos >= topicImgs.length){
                    pos = 0;
                }
                imgTopic.setImageResource(topicImgs[pos]);
                txtTopic.setText(GlobalConstants.topics[pos]);
            }
        });
        btnPrev = (AppCompatButton) findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(--pos < 0){
                    pos = topicImgs.length - 1;
                }
                imgTopic.setImageResource(topicImgs[pos]);
                txtTopic.setText(GlobalConstants.topics[pos]);
            }
        });

        name = getIntent().getStringExtra("name").toString();

        bundle = null;
        bundle = getIntent().getExtras();
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
                // Waiting progress
                ProgressDialog dialog = new ProgressDialog(CreateRoomActivity.this);
                dialog.setMessage("Please wait\nThe room is being setup :)");
                dialog.setCancelable(false);
                dialog.show();

                Intent playIntent = new Intent(CreateRoomActivity.this, GameplayActivity.class);
                Player host = new Player(name, 0, avatar, FriendRequestService.getToken(getApplicationContext()));
                if(Account.isLogged())
                host.setAccountId(Account.getCurrertAccountId());

                Room room = new Room(GlobalConstants.nPoints[maxPointSpinner.getSelectedItemPosition()],
                        GlobalConstants.nPlayers[numPlayerSpinner.getSelectedItemPosition()],
                        GlobalConstants.topics[pos], host);
                room.autoCreateRoomID();
                //create room on firebase
//                String result = CloudFirestore.sendData("ListofRooms", room.getRoomID(), room);
//                System.out.println("Result is: " + result);
//                if(result == "Success"){
//                    playIntent.putExtra("RoomID", room.getRoomID());
//                    startActivity(playIntent);
//                    finish();
//                }



                CloudFirestore.db.collection("ListofRooms").document(room.getRoomID()).set(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        dialog.dismiss();
                        Log.d("create room", room.getRoomID());
                        if (bundle != null) playIntent.putExtras(bundle);
                        playIntent.putExtra("RoomID", room.getRoomID());
                        playIntent.putExtra("Player", (Serializable) host);
                        startActivity(playIntent);
                        finish();
                    }
                });
            }
        });

        btnBack = (TextView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
    }

    public void onClick(View view) {
        super.onBackPressed();

    }
}
