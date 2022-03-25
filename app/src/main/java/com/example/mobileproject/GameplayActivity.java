package com.example.mobileproject;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.fragment.FragmentDrawBox;
import com.example.mobileproject.fragment.FragmentGetDrawing;
import com.example.mobileproject.fragment.FragmentListPlayer;
import com.example.mobileproject.fragment.FragmentResult;
import com.example.mobileproject.fragment.MainCallbacks;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.models.RoomState;
import com.example.mobileproject.models.Topic;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class GameplayActivity extends FragmentActivity implements MainCallbacks {
    FragmentTransaction ft;
    com.example.mobileproject.fragment.FragmentDrawBox FragmentDrawBox;
    com.example.mobileproject.fragment.FragmentListPlayer FragmentListPlayer;
    com.example.mobileproject.fragment.FragmentChatInput FragmentChatInput;
    com.example.mobileproject.fragment.FragmentBoxChat FragmentBoxChat;
    FragmentGetDrawing fragmentGetDrawing;
    FragmentResult fragmentResult;
    ImageButton btnClose;
    public String roomID;
    public Room room;
    public String userName;
    DocumentReference documentReference;
    ProgressBar barHorizontal;
    Handler myHandler = new Handler();
    public final int MAX_PROGRESS = GlobalConstants.TIME_FOR_A_GAME;
    int accum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        roomID = getIntent().getStringExtra("RoomID");
        userName = getIntent().getStringExtra("UserName");
        barHorizontal = (ProgressBar) findViewById(R.id.progress_bar_time);

        documentReference = CloudFirestore.getData("ListofRooms", roomID);


        btnClose = (ImageButton) findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog exitDialog = new AlertDialog.Builder(GameplayActivity.this)
                        .setTitle("Exit the game")
                        .setMessage("Are you sure want to quit this game?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(GameplayActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .setIcon(R.drawable.ic_baseline_sad_face_24)
                        .show(); // do nothing
            }
        });
        // Pop up invitation

        //end pop up invitation


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
                        fragmentResult = FragmentResult.newInstance("Activity");

                        if(room.isPlaying())
                        {
                            // get current progress bar
                            final int[] current_progress_bar = {MAX_PROGRESS};
                            DocumentReference docRef = CloudFirestore.getData("RoomState", roomID);
                            if (docRef != null) {
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        RoomState roomState = documentSnapshot.toObject(RoomState.class);
                                        current_progress_bar[0] = roomState.getTimeLeft();
                                    }
                                });
                            }
                            beginProgressBar(current_progress_bar[0]);
                        }
                        else {
                            ft.replace(R.id.holder_box_draw, FragmentDrawBox);
                        }
                        ft.replace(R.id.holder_list_player, FragmentListPlayer);
                        ft.replace(R.id.holder_chat_input, FragmentChatInput);
                        ft.replace(R.id.holder_chat_box, FragmentBoxChat);
                        ft.commit();
                    }

                }
            });
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(value != null){
                        // new room bị mất user ??
                        Room newRoom = value.toObject(Room.class);
                        // Chỉ chạy nếu drawer thay đổi
                        if(room != null && newRoom != null){
                            if(newRoom.getDrawer() != room.getDrawer()){
                                room = newRoom;
                                beginProgressBar(MAX_PROGRESS);
                                if (room.getPlayers().get(room.getDrawer()).getName().equals(userName)){
                                    RoomState roomState = new RoomState(roomID,
                                            MAX_PROGRESS,
                                            null,
                                            Topic.generateVocab(room.getTopic()));
                                    CloudFirestore.sendData("RoomState", roomID, roomState)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Intent drawIntent = new Intent(GameplayActivity.this, DrawActivity.class);
                                                    drawIntent.putExtra("Timeout", MAX_PROGRESS);
                                                    drawIntent.putExtra("roomID", roomID);
                                                    drawIntent.putExtra("vocab", roomState.getVocab());

                                                    startActivity(drawIntent);
                                                }
                                            });
                                }
                            }
                        }

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
        else if(sender.equals("PLAYER-FLAG")){
            FragmentDrawBox.onMsgFromMainToFragment(strValue);
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
        ft.commitAllowingStateLoss();
        fragmentGetDrawing.onMsgFromMainToFragment("START-FLAG");

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
            fragmentGetDrawing.onMsgFromMainToFragment("END-FLAG"); // kill the getting draw thread
            //result fragment
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.holder_box_draw, fragmentResult);
            ft.commitAllowingStateLoss();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // let owner indicate the next drawer
            if(userName.equals(room.getOwnerUsername())){
                Room newRoom = room.deepcopy();
                if(room.getDrawer() + 1 >= room.getPlayers().size()){
                    newRoom.setDrawer(0);
                }
                else {
                    newRoom.setDrawer(room.getDrawer() + 1);
                }
                CloudFirestore.sendData("ListofRooms", roomID, newRoom);
            }
            // Replace this fragment with result fragment
//            ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.holder_box_draw, FragmentDrawBox);
//            ft.commit();s
            // current drawer will indicate the next drawer

            // Stop SyncDrawing
        }
    };
    //Listen to the stream of room


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        fragmentGetDrawing.onMsgFromMainToFragment("END-FLAG");
        room.removePlayer(userName);
        if(room.getPlayers().size() == 0){
            // If the last person in room left
            // Delete that room
            CloudFirestore.deleteDoc("ListofRooms", roomID);
            CloudFirestore.deleteDoc("RoomState", roomID);
        }
        else {
            CloudFirestore.sendData("ListofRooms", roomID, room);
        }
        System.out.println("call onDestroy");
        super.onDestroy();
    }



}
