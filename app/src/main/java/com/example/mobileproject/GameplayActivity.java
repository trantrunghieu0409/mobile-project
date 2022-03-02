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
    ImageButton btnPopUpInfo;
    ImageButton btnPopUpChat;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);


        list.add(new Player("User 1", 10));
        list.add(new Player("User 2", 5));
        btnPopUpInfo = (ImageButton) findViewById(R.id.btnPopUpInfo);
        btnPopUpChat = (ImageButton) findViewById(R.id.btnPopUpChat);
        listViewPlayer = (ListView) findViewById(R.id.list_player);

        CustomListPlayerAdapter adapter = new CustomListPlayerAdapter(list);
        listViewPlayer.setAdapter(adapter);

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
