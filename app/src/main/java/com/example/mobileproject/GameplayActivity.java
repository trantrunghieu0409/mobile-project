package com.example.mobileproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import com.example.mobileproject.fragment.FragmentDrawBox;
import com.example.mobileproject.fragment.FragmentGetDrawing;
import com.example.mobileproject.fragment.FragmentListPlayer;
import com.example.mobileproject.fragment.MainCallbacks;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.models.Topic;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;



public class GameplayActivity extends FragmentActivity implements MainCallbacks {
    FragmentTransaction ft;
    com.example.mobileproject.fragment.FragmentDrawBox FragmentDrawBox;
    com.example.mobileproject.fragment.FragmentListPlayer FragmentListPlayer;
    com.example.mobileproject.fragment.FragmentChatInput FragmentChatInput;
    com.example.mobileproject.fragment.FragmentBoxChat FragmentBoxChat;
    FragmentGetDrawing fragmentGetDrawing;
    public String roomID;
    public Room room;
    public String userName;
    DocumentReference documentReference;
    ProgressBar barHorizontal;
    Handler myHandler = new Handler();
    public final int MAX_PROGRESS = 5;
    int accum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        roomID = getIntent().getStringExtra("RoomID");
        userName = getIntent().getStringExtra("UserName");
        barHorizontal = (ProgressBar) findViewById(R.id.progress_bar_time);

        documentReference = CloudFirestore.getData("ListofRooms", roomID);

        if(documentReference != null){
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    assert documentSnapshot != null;

                    room = documentSnapshot.toObject(Room.class);
                    if(room != null){
                        ft = getSupportFragmentManager().beginTransaction();

                        FragmentDrawBox = FragmentDrawBox.newInstance(true);
                        FragmentListPlayer = FragmentListPlayer.newInstance(true);
                        FragmentChatInput = FragmentChatInput.newInstance(true);
                        FragmentBoxChat = FragmentBoxChat.newInstance(true);
                        fragmentGetDrawing = FragmentGetDrawing.newInstance(room.getRoomID());

                        ft.replace(R.id.holder_box_draw, FragmentDrawBox);
                        ft.replace(R.id.holder_list_player, FragmentListPlayer);
                        ft.replace(R.id.holder_chat_input, FragmentChatInput);
                        ft.replace(R.id.holder_chat_box, FragmentBoxChat);
                        ft.commit();
                    }

                }
            });
        }
    }

    @Override
    public void onMsgFromFragToMain(String sender, String strValue) {
        if (sender.equals("MESS-FLAG")) {
            FragmentBoxChat.onMsgFromMainToFragment(strValue);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void beginProgressBar(int MAX_PROGRESS){
        accum = MAX_PROGRESS;
        barHorizontal.setMax(MAX_PROGRESS);
        barHorizontal.setProgress(accum);

        // start sync drawing fragment
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.holder_box_draw, fragmentGetDrawing);
        ft.commit();

        Thread backgroundThread = new Thread(backgroundTime,"isBarTime");
        backgroundThread.start();
    }

    private Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {
            accum--;
            barHorizontal.setProgress(accum);
            if(accum < 0){
                // Do something
            }
        }
    };

    private Runnable backgroundTime = new Runnable() {
        @Override
        public void run() {
            for(int i = 0; i < MAX_PROGRESS;i++){
                try {
                    Thread.sleep(1000);
                    myHandler.post(foregroundRunnable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            fragmentGetDrawing.onMsgFromMainToFragment("END-FLAG");
            // Replace this fragment with result fragment
//            ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.holder_box_draw, FragmentDrawBox);
//            ft.commit();
            // current drawer will indicate the next drawer

            // Stop SyncDrawing
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if (userName != null)
            room.removePlayer(userName);
        if(room.getPlayers().size() == 0){
            // If the last person in room left
            // Delete that room
            CloudFirestore.deleteDoc("ListofRooms", roomID);
        }
        else {
            CloudFirestore.sendData("ListofRooms", roomID, room);
        }
    }
    public void notiDraw(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();

        //this is custom dialog
        //custom_popup_dialog contains textview only
        View customView = layoutInflater.inflate(R.layout.popup_notidraw, null);
        // reference the textview of custom_popup_dialog
        Button buttonDrawTurn = customView.findViewById(R.id.buttonDrawTurn);


        buttonDrawTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent drawIntent = new Intent(GameplayActivity.this, DrawActivity.class);
                drawIntent.putExtra("Timeout", MAX_PROGRESS);
                drawIntent.putExtra("roomID", roomID);
                drawIntent.putExtra("vocab", Topic.generateVocab(room.getTopic()));
                startActivity(drawIntent);
                System.out.println("End draw");
            }
        });

        builder.setView(customView);
        builder.create();
        builder.show();
    }
}
