package com.example.mobileproject;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import com.example.mobileproject.fragment.MainCallbacks;
import com.example.mobileproject.models.Room;
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

    public String roomID;
    public Room room;
    public String userName;
    DocumentReference documentReference;
    ProgressBar barHorizontal;
    Handler myHandler = new Handler();
    public final int MAX_PROGRESS = 20;
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
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
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
}
