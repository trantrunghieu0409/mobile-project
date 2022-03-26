package com.example.mobileproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobileproject.draw_config.Config;
import com.example.mobileproject.models.Player;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.utils.CloudFirestore;
import com.example.mobileproject.utils.FriendRequestService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class HomeActivity extends Activity {
    String[] languages = {"Tiếng Việt", "English"};
    Integer[] avatars = Config.Avatars;

    Button btnPlay;
    Button btnCreateRoom;
    ImageButton btnNext;
    ImageButton btnPrev;
    ImageView btnLogin;
    CircularImageView avatar;
    EditText edtName;
    Player newPlayer;

    int pos = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        FriendRequestService.createToken(getApplicationContext());

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnCreateRoom = (Button) findViewById(R.id.btnCreateRoom);
        Spinner spinner = (Spinner) findViewById(R.id.languages_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, languages);
        spinner.setAdapter(adapter);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPrev = (ImageButton) findViewById(R.id.btnPrev);
        avatar = (CircularImageView) findViewById(R.id.circle_avatar);
        edtName = (EditText) findViewById(R.id.edtName);
        btnLogin = (ImageView) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to login here
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(++pos >= avatars.length){
                    pos = 0;
                }
                avatar.setImageResource(avatars[pos]);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(--pos < 0){
                    pos = avatars.length -1;
                }
                avatar.setImageResource(avatars[pos]);
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtName.getText().length() == 0){
                    // Pop up Error message
                    edtName.requestFocus();
                }
                else {
                    //go to play screen here
                    CloudFirestore.getListofRooms().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Random rand = new Random();
                            List<DocumentSnapshot> listRooms = task.getResult().getDocuments();
                            if (listRooms.size() == 0) {
                                // do something here -> pop up notify that there is no room to play
                                AlertDialog noRoomDialog = new AlertDialog.Builder(HomeActivity.this)
                                        .setTitle("No room to play")
                                        .setMessage("There is no active room now. Want to create one ?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                DocumentSnapshot documentSnapshot = listRooms.get(rand.nextInt(listRooms.size()));
                                                Room room = documentSnapshot.toObject(Room.class);
                                                assert room != null;
                                                newPlayer = new Player(edtName.getText().toString(), 0, avatars[pos], FriendRequestService.getToken(getApplicationContext()));
                                                room.addPlayer(newPlayer);
                                                Intent playIntent = new Intent(HomeActivity.this, GameplayActivity.class);
                                                CloudFirestore.db.collection("ListofRooms").document(room.getRoomID()).set(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        playIntent.putExtra("RoomID", room.getRoomID());
                                                        playIntent.putExtra("Player", (Serializable) newPlayer);
                                                        startActivity(playIntent);
                                                        finish();
                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .setIcon(R.drawable.ic_baseline_sad_face_24)
                                        .show(); // do nothing

                            }
                            else {
                                popupPlay(listRooms);
                            }
                        }
                    });
                }
            }
        });
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtName.getText().length() == 0){
                    // Pop up Error message
                    edtName.requestFocus();
                }
                else {
                    Intent roomIntent = new Intent(HomeActivity.this, CreateRoomActivity.class);
                    roomIntent.putExtra("name", edtName.getText().toString());
                    roomIntent.putExtra("avatar", avatars[pos]);
                    startActivityForResult(roomIntent, 0);
                }
            }
        });
    }
    public void popupPlay(List<DocumentSnapshot> listRooms){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();

        //this is custom dialog
        //custom_popup_dialog contains textview only
        View customView = layoutInflater.inflate(R.layout.popup_chooseplay, null);
        // reference the textview of custom_popup_dialog

        EditText edtCode = customView.findViewById(R.id.edtCode);
        Button randomButton = customView.findViewById(R.id.randomButton);
        Button playButton = customView.findViewById(R.id.playButton);
        builder.setView(customView);
        AlertDialog alert = builder.create();
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rand = new Random();
                DocumentSnapshot documentSnapshot = listRooms.get(rand.nextInt(listRooms.size()));
                Room room = documentSnapshot.toObject(Room.class);
                assert room != null;
                newPlayer = new Player(edtName.getText().toString(), 0, avatars[pos], FriendRequestService.getToken(getApplicationContext()));
                room.addPlayer(newPlayer);
                Intent playIntent = new Intent(HomeActivity.this, GameplayActivity.class);
                // Join room
                CloudFirestore.db.collection("ListofRooms").document(room.getRoomID()).set(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        playIntent.putExtra("RoomID", room.getRoomID());
                        playIntent.putExtra("Player", (Serializable) newPlayer);
                        startActivity(playIntent);
                        alert.dismiss();
                        finish();
                    }
                });
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Join room
                DocumentReference documentReference = CloudFirestore.getData("ListofRooms", edtCode.getText().toString());
                if (documentReference != null) {
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Room room = documentSnapshot.toObject(Room.class);
                            newPlayer = new Player(edtName.getText().toString(), 0, avatars[pos], FriendRequestService.getToken(getApplicationContext()));
                            room.addPlayer(newPlayer);
                            Intent playIntent = new Intent(HomeActivity.this, GameplayActivity.class);
                            CloudFirestore.db.collection("ListofRooms").document(room.getRoomID()).set(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    playIntent.putExtra("RoomID", room.getRoomID());
                                    playIntent.putExtra("Player", (Serializable) newPlayer);
                                    startActivity(playIntent);
                                    alert.dismiss();
                                    finish();
                                }
                            });
                        }
                    });
                }
                else {
                    // pop up room not found
                    AlertDialog notfoundDialog = new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Room not found")
                            .setMessage("Room not found")
                            .setNegativeButton("OK", null)
                            .setIcon(R.drawable.ic_baseline_sad_face_24)
                            .show(); // do nothing
                }
            }
        });
        alert.show();
    }
}
