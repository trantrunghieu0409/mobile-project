package com.example.mobileproject;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mobileproject.adapter.CustomImageAdapter;

import java.util.Arrays;

public class EditProfileActivity extends Activity {
    RelativeLayout btnChangeAvatar, btnChangeUsername, btnChangeNation, btnChangePassword;
    ImageButton btnClose;
    ImageView avatar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        avatar = (ImageView) findViewById(R.id.imgAvatar);

        btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> { finish(); });

        btnChangeAvatar = (RelativeLayout) findViewById(R.id.btnChangeAvatar);
        btnChangeUsername = (RelativeLayout) findViewById(R.id.btnChangeUsername);
        btnChangeNation = (RelativeLayout) findViewById(R.id.btnChangeNation);
        btnChangePassword = (RelativeLayout) findViewById(R.id.btnChangePassword);

        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                // create a pop up
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupAvatarList = inflater.inflate(R.layout.popup_avatarlist, null);

                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // tap outside to dismiss this pop up
                final PopupWindow popupWindow = new PopupWindow(popupAvatarList, width, height, focusable);

                // show a pop up
                popupWindow.showAtLocation(view, Gravity.CENTER, 0 , 0);

                Integer[] thumbnails = {R.drawable.avatar_batman, R.drawable.avatar};

                // handle click image and click button to change avatar
                final GridView gridViewAvatar = (GridView) popupAvatarList.findViewById(R.id.gridViewAvatar);
                gridViewAvatar.setAdapter(new CustomImageAdapter(EditProfileActivity.this, thumbnails));


                final int[] chosenPos = {Arrays.asList(thumbnails).indexOf((Integer) avatar.getTag())};
                gridViewAvatar.setItemChecked(chosenPos[0], true);

                gridViewAvatar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i != chosenPos[0]) {
                            view.setBackgroundColor(R.drawable.red_background_border);

                            chosenPos[0] = i;
                       }

                    }
                });

                // confirm
                final Button btnOk = (Button) popupAvatarList.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // change avatar here :)
                        System.out.println("pos" + chosenPos[0]);
                        avatar.setTag(thumbnails[chosenPos[0]]);
                        popupWindow.dismiss();
                    }
                });
            }
        });

        btnChangeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnChangeNation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
}}
