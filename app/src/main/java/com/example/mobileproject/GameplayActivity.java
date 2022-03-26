package com.example.mobileproject;

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.example.mobileproject.fragment.FragmentNotiDrawer;
import com.example.mobileproject.fragment.FragmentResult;
import com.example.mobileproject.fragment.MainCallbacks;
import com.example.mobileproject.models.Chat;
import com.example.mobileproject.models.Player;
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
    FragmentNotiDrawer fragmentNotiDrawer;
    ImageButton btnClose;
    public String roomID;
    public Room room;
    public Player mainPlayer;
    DocumentReference documentReference;
    ProgressBar barHorizontal;
    Handler myHandler = new Handler();
    public final int MAX_PROGRESS_DRAWING = GlobalConstants.TIME_FOR_A_GAME;
    public final int MAX_PROGRESS_WAITING = GlobalConstants.TIME_FOR_A_WAITING;
    int accum = 0;
    int flagNextActivity = 1;

    private String Vocab;
    private RoomState roomState;
    public Player currentDrawing;
    PopupWindow popupWindow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);
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

                        ft.replace(R.id.holder_box_draw, FragmentDrawBox);
                        ft.replace(R.id.holder_list_player, FragmentListPlayer);
                        ft.replace(R.id.holder_chat_input, FragmentChatInput);
                        ft.replace(R.id.holder_chat_box, FragmentBoxChat);
                        ft.commit();

                        // notification user join for everyone in popup chat
                        Chat chat = new Chat(mainPlayer.getName() + " joined");
                        documentReference.collection("ChatPopUp").document(chat.getTimestamp()).set(chat);
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
                                Vocab = Topic.generateVocab(room.getTopic());
                                roomState = new RoomState(roomID,
                                        MAX_PROGRESS_DRAWING,
                                        null,
                                        Vocab);
                                currentDrawing = room.getPlayers().get(room.getDrawer());
                                flagNextActivity = 1;
                                processNotiDrawer(currentDrawing,Vocab);

                            }
                        }

                    }
                }
            });
        }
    }


    public void processNotiDrawer(Player currentDrawing,String vocab){
        beginProgressBar(MAX_PROGRESS_WAITING);
        ft = getSupportFragmentManager().beginTransaction();
        String str = currentDrawing.getName()+"`"+String.valueOf(currentDrawing.getAvatar());
        ft.replace(R.id.holder_box_draw,fragmentNotiDrawer);
        // Set result
        fragmentNotiDrawer.onMsgFromMainToFragment(str);
        fragmentResult.onMsgFromMainToFragment(Vocab);
        ft.commit();
        if(mainPlayer.getName().equals(currentDrawing.getName())){
            popupNotiDraw(vocab);
        }

    }

    public void processDrawing(Player currentDrawing,RoomState roomState){
        beginProgressBar(MAX_PROGRESS_DRAWING);
        if (currentDrawing.getName().equals(mainPlayer.getName())){
            CloudFirestore.sendData("RoomState", roomID, roomState)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent drawIntent = new Intent(GameplayActivity.this, DrawActivity.class);
                            drawIntent.putExtra("Timeout", MAX_PROGRESS_DRAWING);
                            drawIntent.putExtra("roomID", roomID);
                            drawIntent.putExtra("vocab", roomState.getVocab());
                            popupWindow.dismiss();
                            startActivity(drawIntent);
                        }
                    });
        }
        else{
            // start sync drawing fragment
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.holder_box_draw, fragmentGetDrawing);
            ft.commit();
            fragmentGetDrawing.onMsgFromMainToFragment("START-FLAG");
        }
    }

    public void processResult(){
        beginProgressBar(MAX_PROGRESS_WAITING);
        if (!currentDrawing.getName().equals(mainPlayer.getName())){
            fragmentGetDrawing.onMsgFromMainToFragment("END-FLAG");// kill the getting draw thread
        }
        //result fragment
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.holder_box_draw, fragmentResult);
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


    public void beginProgressBar(int max_process){
        accum = max_process;
        barHorizontal.setMax(max_process);
        barHorizontal.setProgress(accum);

        Thread backgroundThread = new Thread(backgroundTime,"isBarTime");
        backgroundThread.start();
    }

    private Runnable foregroundRunnable = new Runnable() {
        @Override
        public void run() {
            accum--;
            barHorizontal.setProgress(accum);
            if(accum <= 0) {
                // Do something
            }
        }
    };

    private Runnable backgroundTime = new Runnable() {
        @Override
        public void run() {
            int max = 0;
            if(flagNextActivity == 1 || flagNextActivity == 3) max = MAX_PROGRESS_WAITING;
            else if(flagNextActivity == 2){
                max = MAX_PROGRESS_DRAWING;
            }
            for(int i = 0; i < max;i++){
                try {
                    Thread.sleep(1000);
                    myHandler.post(foregroundRunnable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            flagNextActivity++;
            if(flagNextActivity > 4){
                flagNextActivity = 1;
            }
            if(flagNextActivity == 2){
                processDrawing(currentDrawing,roomState);
            }
            if(flagNextActivity == 3){
                processResult();
            }
            if(flagNextActivity == 4){
                nextDrawer();
            }

        }
    };
    //Listen to the stream of room


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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        fragmentGetDrawing.onMsgFromMainToFragment("END-FLAG");
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
        System.out.println("call onDestroy");
        super.onDestroy();
    }

    public void popupNotiDraw(String vocab){

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup_notidraw = inflater.inflate(R.layout.popup_notidraw, null);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(popup_notidraw, width, height, true);

        TextView Vocab = popup_notidraw.findViewById(R.id.txt_vocab_popup_notidraw);
        Vocab.setText(vocab);

        popupWindow.showAtLocation(popup_notidraw,Gravity.CENTER, 0 , 0);
    }



}
