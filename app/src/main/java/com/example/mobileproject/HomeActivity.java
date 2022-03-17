package com.example.mobileproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobileproject.draw_config.Config;
import com.example.mobileproject.models.Player;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends Activity {
    String[] languages = {"Vietnamese", "English"};
    Integer[] avatars = Config.Avatars;

    Button btnPlay;
    Button btnCreateRoom;
    ImageButton btnNext;
    ImageButton btnPrev;
    ImageView btnLogin;
    CircularImageView avatar;
    EditText edtName;

    int pos = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
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
                }
                else {
                    //go to play screen here
                    CloudFirestore.getListofRooms().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Random rand = new Random();
                            List<DocumentSnapshot> listRooms = task.getResult().getDocuments();
                            DocumentSnapshot documentSnapshot = listRooms.get(rand.nextInt(listRooms.size()));
                            Room room = documentSnapshot.toObject(Room.class);
                            assert room != null;
                            room.addPlayer(new Player(edtName.getText().toString(), 0, avatars[pos]));
                            Intent playIntent = new Intent(HomeActivity.this, GameplayActivity.class);
                            CloudFirestore.db.collection("ListofRooms").document(room.getRoomID()).set(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    playIntent.putExtra("RoomID", room.getRoomID());
                                    startActivity(playIntent);
                                    finish();
                                }
                            });
                        }
                    });
                }
            }
        });
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent roomIntent = new Intent(HomeActivity.this, CreateRoomActivity.class);
                roomIntent.putExtra("name", edtName.getText().toString());
                roomIntent.putExtra("avatar", avatars[pos]);
                startActivityForResult(roomIntent, 0);
            }
        });
    }
}
