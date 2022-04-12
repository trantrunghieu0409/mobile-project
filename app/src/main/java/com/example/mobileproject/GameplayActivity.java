package com.example.mobileproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.fragment.FragmentGameOver;
import com.example.mobileproject.fragment.FragmentGetDrawing;
import com.example.mobileproject.fragment.FragmentNotiDrawer;
import com.example.mobileproject.fragment.FragmentResult;
import com.example.mobileproject.fragment.MainCallbacks;
import com.example.mobileproject.models.Account;
import com.example.mobileproject.models.Chat;
import com.example.mobileproject.models.Player;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.models.RoomState;
import com.example.mobileproject.models.Topic;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;


public class GameplayActivity extends FragmentActivity implements MainCallbacks {
    FragmentTransaction ft;
    com.example.mobileproject.fragment.FragmentDrawBox FragmentDrawBox;
    com.example.mobileproject.fragment.FragmentListPlayer FragmentListPlayer;
    com.example.mobileproject.fragment.FragmentChatInput FragmentChatInput;
    com.example.mobileproject.fragment.FragmentBoxChat FragmentBoxChat;
    FragmentGetDrawing fragmentGetDrawing;
    FragmentResult fragmentResult;
    FragmentNotiDrawer fragmentNotiDrawer;
    FragmentGameOver fragmentGameOver;
    ImageButton btnClose;
    public String roomID;
    public Room room;
    public Player mainPlayer;
    DocumentReference documentReference;
    ProgressBar barHorizontal;
    Handler myHandler = new Handler();
    public final int MAX_PROGRESS_DRAWING = GlobalConstants.TIME_FOR_A_GAME;
    public final int MAX_PROGRESS_WAITING = GlobalConstants.TIME_FOR_A_WAITING;
    public int accum = 0;
    int flagCurrentActivity = 0;
    boolean stillPlaying = false;

    private String Vocab;
    private RoomState roomState;
    private Thread barThread;
    public Player currentDrawing;
    PopupWindow popupWindow;
    public Account account;
    Bundle bundle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
        account = null;
        //get Account
        bundle = null;
        bundle = getIntent().getExtras();
        if (bundle != null) {
            account = (Account) bundle.getSerializable("account");
        }
        //
        roomID = getIntent().getStringExtra("RoomID");
        mainPlayer = (Player) getIntent().getSerializableExtra("Player");
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
                                if (bundle != null) intent.putExtras(bundle);
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
                        fragmentNotiDrawer = FragmentNotiDrawer.newInstance(true);
                        fragmentGameOver = FragmentGameOver.newInstance(true);

                        ft.replace(R.id.holder_box_draw, FragmentDrawBox);
                        ft.replace(R.id.holder_list_player, FragmentListPlayer);
                        ft.replace(R.id.holder_chat_input, FragmentChatInput);
                        ft.replace(R.id.holder_chat_box, FragmentBoxChat);
                        ft.commit();

                        if(room.getDrawer() != -1){
                            flagCurrentActivity = room.getFlagCurrentActivity();
                            currentDrawing = room.getPlayers().get(room.getDrawer());
                        }

                        // notification user join for everyone in popup chat
                        if(room.getPlayers().size() > 1){
                            Chat chat = new Chat(mainPlayer.getName() + " joined");
                            documentReference.collection("ChatPopUp").document(chat.getTimestamp()).set(chat);
                        }
                    }

                }
            });
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(value != null){
                        // new room bị mất user ??
                        Room newRoom = value.toObject(Room.class);
                        if(room != null && newRoom != null){
                            // Chỉ chạy nếu drawer thay đổi
                            if(newRoom.getDrawer() != room.getDrawer()){
                                room = newRoom;

                                if(room.getPlayers().size() == 1 && room.getDrawer() == -1){
                                    stillPlaying = false;
                                    ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.holder_box_draw, FragmentDrawBox);
                                    ft.commitAllowingStateLoss();
                                }
                                // Just ownerRoom can change
                                else if (room.getOwnerUsername().equals(mainPlayer.getName())) {
                                    currentDrawing = room.getPlayers().get(room.getDrawer());
                                    RoomState roomState = new RoomState(roomID,
                                            MAX_PROGRESS_DRAWING,
                                            null,
                                            Topic.generateVocab(room.getTopic()));

                                    CloudFirestore.sendData("RoomState", roomID, roomState);
                                    room.setFlagCurrentActivity(1);
                                    room.setFirstAnswer(true);
                                    room.resetAllStatusPlayer();
                                    room.findPlayerAndSetStatus(currentDrawing.getName(),2);
                                    CloudFirestore.sendData("ListofRooms", roomID, room);
                                }
                            }


                            // Receive activity's room
                            if(newRoom.getFlagCurrentActivity() != room.getFlagCurrentActivity()){
                                room = newRoom;
                                flagCurrentActivity = room.getFlagCurrentActivity();
                                Objects.requireNonNull(CloudFirestore.getData("RoomState", roomID)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        roomState = documentSnapshot.toObject(RoomState.class);
                                        fragmentGetDrawing.onMsgFromMainToFragment("END-FLAG");
                                        if(roomState != null){
                                            Vocab = roomState.getVocab();
                                            currentDrawing = room.getPlayers().get(room.getDrawer());

                                            if(flagCurrentActivity == 1){
                                                processNotiDrawer(currentDrawing, Vocab);
                                            }
                                            if(flagCurrentActivity == 2){
                                                processDrawing(currentDrawing,roomState);
                                            }
                                            if(flagCurrentActivity == 3){
                                                processResult();
                                            }
                                            if(flagCurrentActivity == 4){
                                                nextDrawer();
                                            }
                                            if(flagCurrentActivity == 5){
                                                processGameOver();
                                            }
                                            if(flagCurrentActivity == 6){
                                                processResetRoom();
                                            }
                                        }
                                    }
                                });
                            }

                            if(newRoom.getFlagCurrentActivity() == 0 && newRoom.getPlayers().size() == 1){
                                FragmentDrawBox.onMsgFromMainToFragment("CLOSE");
                            }

                            //Change owner room when awaiting
                            if(newRoom.getFlagCurrentActivity() == 0 && !newRoom.getOwnerUsername().equals(room.getOwnerUsername())){
                                room = newRoom;
                                FragmentDrawBox = FragmentDrawBox.newInstance(true);
                                //Reset fragment
                                ft = getSupportFragmentManager().beginTransaction();
                                ft.replace(R.id.holder_box_draw, FragmentDrawBox);
                                ft.commitAllowingStateLoss();
                            }


                            // Check kick
                            if(newRoom.checkVote()){
                                room = newRoom;
                                room.setVote(0);
                                if(currentDrawing.getName().equals(mainPlayer.getName())){
                                    processKickPlayer();
                                }
                            }

                        }
                    }
                }
            });
        }

    }


    public void processNotiDrawer(Player currentDrawing,String vocab){
        stillPlaying = true;
        beginProgressBar(MAX_PROGRESS_WAITING);
        ft = getSupportFragmentManager().beginTransaction();
        String str = currentDrawing.getName()+"`"+ currentDrawing.getAvatar();
        ft.replace(R.id.holder_box_draw,fragmentNotiDrawer);
        fragmentNotiDrawer.onMsgFromMainToFragment(str);
        ft.commitAllowingStateLoss();
        // Block Answer Chat
        FragmentChatInput.onMsgFromMainToFragment("`RIGHT`");
        //Reset
        FragmentBoxChat.onMsgFromMainToFragment("`Reset`");
        if(mainPlayer.getName().equals(currentDrawing.getName())){
            popupNotiDraw(vocab);
            FragmentBoxChat.onMsgFromMainToFragment("`TURNFOR`" + currentDrawing.getName());
        }

    }

    public void processDrawing(Player currentDrawing,RoomState roomState){
        beginProgressBar(MAX_PROGRESS_DRAWING);
        if (currentDrawing.getName().equals(mainPlayer.getName())){
            Intent drawIntent = new Intent(GameplayActivity.this, DrawActivity.class);
            drawIntent.putExtra("Timeout", MAX_PROGRESS_DRAWING);
            drawIntent.putExtra("roomID", roomID);
            drawIntent.putExtra("vocab", roomState.getVocab());
            if (bundle != null) drawIntent.putExtras(bundle);
            if(popupWindow != null){
                popupWindow.dismiss();
            }
            stillPlaying = true;
            startActivity(drawIntent);
        }
        else{
            // Unblock answer chat
            FragmentChatInput.onMsgFromMainToFragment("`Reset`");
            // start sync drawing fragment
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.holder_box_draw, fragmentGetDrawing);
            ft.commitAllowingStateLoss();
            fragmentGetDrawing.onMsgFromMainToFragment("START-FLAG");
        }
    }

    public void processResult(){
        beginProgressBar(MAX_PROGRESS_WAITING);
        if (!currentDrawing.getName().equals(mainPlayer.getName())){
            fragmentGetDrawing.onMsgFromMainToFragment("END-FLAG");// kill the getting draw thread
        }
        else{
            FragmentBoxChat.onMsgFromMainToFragment("`ANSWER`");
        }
        //Block report
        FragmentChatInput.onMsgFromMainToFragment("`REPORT-BLOCK`");
        // Block Answer Chat
        FragmentChatInput.onMsgFromMainToFragment("`RIGHT`");
        //result fragment
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.holder_box_draw, fragmentResult);
        fragmentResult.onMsgFromMainToFragment(Vocab);
        ft.commitAllowingStateLoss();
    }

    public void nextDrawer(){
        // let owner indicate the next drawer
        if(mainPlayer.getName().equals(room.getOwnerUsername())){
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

    public void processGameOver(){
        beginProgressBar(MAX_PROGRESS_WAITING);
        // Block Answer Chat
        FragmentChatInput.onMsgFromMainToFragment("`RIGHT`");
        //Gameover fragment
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.holder_box_draw, fragmentGameOver);
        ft.commitAllowingStateLoss();
        if(!mainPlayer.getAccountId().isEmpty()){
            if(account != null){
                Boolean[] place = room.getTopRoom(mainPlayer.getName());
                account.updateAnalystAccount(place);
            }
        }
    }

    public void processKickPlayer(){
        Intent intent = new Intent(GameplayActivity.this, HomeActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra("isKick",true);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void processResetRoom(){
        room.resetAllPointPlayer();
        room.setFlagCurrentActivity(0);
        room.setDrawer(-1);
        CloudFirestore.sendData("ListofRooms", roomID, room);

        //Reset fragment
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.holder_box_draw, FragmentDrawBox);
        ft.commitAllowingStateLoss();
    }

    public void beginProgressBar(int max_process){
        accum = max_process;
        barHorizontal.setMax(max_process);
        barHorizontal.setProgress(accum);

        if(barThread != null &&barThread.isAlive()) {
            barThread.interrupt();
        }
        barThread = new Thread(backgroundTime,"isBarTime");
        barThread.start();
    }

    public void handleExitPlayer(){
        // Handle change drawer
        if(room.getPlayers().size() == 1){
            RemovePlayer();
            return;
        }
        if(room.getPlayers().size() <= 2){
            room.setDrawer(-1);
        }
        else if(currentDrawing != null){
            if(currentDrawing.getName().equals(mainPlayer.getName())){
                nextDrawer();
                stillPlaying = false;
            }
        }
        RemovePlayer();
    }


    public void RemovePlayer(){
        //Remove
        if(fragmentGetDrawing != null){
            fragmentGetDrawing.onMsgFromMainToFragment("END-FLAG");
        }
        room.removePlayer(mainPlayer.getName());
        if(room.getPlayers().size() == 0){
            // If the last person in room left
            // Delete that room
            CloudFirestore.deleteDoc("ListofRooms", roomID);
            CloudFirestore.deleteDoc("RoomState", roomID);
        }
        else {
            CloudFirestore.sendData("ListofRooms", roomID, room);
        }
    }

    private final Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {

            accum--;
            barHorizontal.setProgress(accum);
            // Do something
        }
    };

    private final Runnable backgroundTime = new Runnable() {
        @Override
        public void run() {
            int max = 0;
            if(flagCurrentActivity == 1 || flagCurrentActivity == 3 || flagCurrentActivity == 5) max = MAX_PROGRESS_WAITING;
            else if(flagCurrentActivity == 2){
                max = MAX_PROGRESS_DRAWING;
            }
            for(int i = 0; i < max && stillPlaying ;i++){
                try {
                    Thread.sleep(1000);
                    myHandler.post(foregroundRunnable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!stillPlaying){
                accum = 0;
                myHandler.post(foregroundRunnable);
            }else if (currentDrawing.getName().equals(mainPlayer.getName()) && stillPlaying && room.getPlayers().size() > 1) {
                flagCurrentActivity++;
                if(room.checkPlayerReachMaxScore()){
                    if(flagCurrentActivity != 6){
                        flagCurrentActivity = 5;
                    }
                    if(flagCurrentActivity == 6){
                        stillPlaying = false;
                    }
                }
                else{
                    if(flagCurrentActivity > 4){
                        flagCurrentActivity = 1;
                    }
                }

                room.setFlagCurrentActivity(flagCurrentActivity);
                CloudFirestore.sendData("ListofRooms", roomID, room);
                room.setFlagCurrentActivity(0);
            }
        }
    };
    //Listen to the stream of room


    @Override
    public void onMsgFromFragToMain(String sender, String strValue) {
        switch (sender) {
            case "MESS-FLAG":
                FragmentBoxChat.onMsgFromMainToFragment(strValue);
                break;
            case "PLAYER-FLAG":
                FragmentDrawBox.onMsgFromMainToFragment(strValue);
                break;
            case "RIGHT-FLAG":
                FragmentChatInput.onMsgFromMainToFragment(strValue);
                break;
            case "REPORT-FLAG":
                FragmentBoxChat.onMsgFromMainToFragment("`REPORT`" + strValue);
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
//        if(stillPlaying){
//            stillPlaying = false;
//        }
//        else {
//            handleExitPlayer();
//        }
        if(!stillPlaying){
            handleExitPlayer();
        }
        System.out.println("STOP");

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        handleExitPlayer();
        System.out.println("Destroy");
        FragmentChatInput.onMsgFromMainToFragment(mainPlayer.getName() + " left");
        if(barThread != null &&barThread.isAlive()) {
            barThread.interrupt();
        }
        super.onDestroy();
    }

    public void popupNotiDraw(String vocab){
        LayoutInflater inflater = (LayoutInflater) LayoutInflater.from(GameplayActivity.this);
        View popup_notidraw = inflater.inflate(R.layout.popup_notidraw,null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popup_notidraw, width, height, true);

        TextView Vocab = popup_notidraw.findViewById(R.id.txt_vocab_popup_notidraw);
        Vocab.setText(vocab);


        popupWindow.showAtLocation(popup_notidraw,Gravity.CENTER, 0 , 0);
    }



}
