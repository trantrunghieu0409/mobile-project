package com.example.mobileproject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

class Player {
    private String name;
    private int point;
    public Player(String name, int point){
        this.name = name;
        this.point = point;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }
}

public class GameplayActivity extends Activity {
    ArrayList<Player> list = new ArrayList<>();
    ListView listViewPlayer;
    boolean audio;
    boolean report;
    ImageButton btnReport;
    ImageButton btnAudio;
    ImageButton btnPopUpInfo;
    ImageButton btnPopUpChat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);


        list.add(new Player("User 1", 10));
        list.add(new Player("User 2", 5));
        audio = true;
        report = false;
        btnReport = (ImageButton) findViewById(R.id.btnReport);
        btnAudio = (ImageButton) findViewById(R.id.btnAudio);
        btnPopUpInfo = (ImageButton) findViewById(R.id.btnPopUpInfo);
        btnPopUpChat = (ImageButton) findViewById(R.id.btnPopUpChat);
        listViewPlayer = (ListView) findViewById(R.id.list_player);

        CustomListPlayerAdapter adapter = new CustomListPlayerAdapter(list);
        listViewPlayer.setAdapter(adapter);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!report){
                    btnReport.setAlpha(0.8f);
                    btnReport.setEnabled(false);
                }
            }
        });

        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(audio){
                    btnAudio.setImageResource(R.drawable.ic_baseline_volume_off_24);
                    audio = false;
                }
                else{
                    btnAudio.setImageResource(R.drawable.ic_baseline_volume_up_24);
                    audio = true;
                }
            }
        });

        btnPopUpInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogInfo();
            }
        });

        btnPopUpChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogChat();
            }
        });



    }

    private void showDialogInfo(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_info);
        dialog.show();
    }
    private void showDialogChat(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_chat);

        ImageView btnClose = dialog.findViewById(R.id.btnClosePopUpChat);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
