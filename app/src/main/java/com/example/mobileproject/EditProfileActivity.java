package com.example.mobileproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.mobileproject.adapter.CustomImageAdapter;
import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.models.Account;

import java.util.Arrays;

public class EditProfileActivity extends Activity {
    RelativeLayout btnChangeAvatar, btnChangeUsername, btnChangeNation, btnChangePassword;
    EditText txtUsername;
    ImageButton btnClose;
    ImageView avatar;
    Button btnSave;
    Account account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        account = (Account) bundle.getSerializable("account");


        avatar = (ImageView) findViewById(R.id.imgAvatar);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        avatar.setImageResource(account.getAvatar());
        txtUsername.setText(account.getName());

        btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> { finish(); });

        btnChangeAvatar = (RelativeLayout) findViewById(R.id.btnChangeAvatar);
        btnChangeUsername = (RelativeLayout) findViewById(R.id.btnChangeUsername);
        btnChangeNation = (RelativeLayout) findViewById(R.id.btnChangeNation);
        btnChangePassword = (RelativeLayout) findViewById(R.id.btnChangePassword);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                // create a pop up
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupAvatarList = inflater.inflate(R.layout.popup_avatarlist, null);

                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popupWindow = new PopupWindow(popupAvatarList, width, height, true);

                // show a pop up
                popupWindow.showAtLocation(view, Gravity.CENTER, 0 , 0);

                // handle click image and click button to change avatar
                final GridView gridViewAvatar = (GridView) popupAvatarList.findViewById(R.id.gridViewAvatar);
                gridViewAvatar.setAdapter(new CustomImageAdapter(EditProfileActivity.this, GlobalConstants.thumbnails));
                final int[] chosenPos = {-1};
                gridViewAvatar.setOnItemClickListener((adapterView, view12, i, l) -> {
                    if (chosenPos[0] != -1) {
                        View v = gridViewAvatar.getChildAt(chosenPos[0]);
                        v.setBackgroundColor(124333); // initial background color of gridview
                    }
                    chosenPos[0] = i;
                    view12.setBackgroundColor(R.drawable.red_background_border);
                });


                // confirm
                final Button btnSave = (Button) popupAvatarList.findViewById(R.id.btnSave);
                btnSave.setText("Save changes");
                btnSave.setOnClickListener(view1 -> {
                    if (chosenPos[0] != -1) {
                        int avatarSrc = GlobalConstants.thumbnails[chosenPos[0]];
                        avatar.setImageResource(avatarSrc);
                        account.setAvatar(avatarSrc);
                    }
                    else {
                        // do nothing
                    }
                    popupWindow.dismiss();
                });
            }
        });

        btnChangeUsername.setOnClickListener(view -> {

        });
        btnChangeNation.setOnClickListener(view -> {

        });
}}
