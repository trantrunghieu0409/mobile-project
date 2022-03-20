package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.events.OnSwipeTouchListener;
import com.example.mobileproject.fragment.FragmentListFriends;
import com.example.mobileproject.fragment.FragmentProfile;
import com.example.mobileproject.fragment.MainCallbacks;
import com.example.mobileproject.models.Account;
import com.google.firebase.firestore.DocumentReference;

public class ProfileActivity extends FragmentActivity implements MainCallbacks {
    FragmentTransaction ft; FragmentProfile profileFragment; FragmentListFriends listFriendsFragment;
    ImageView imgAvatar;
    TextView txtUsername, txtAchievements;
    LinearLayout btnEditProfile;
    Account account;
    String accountName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtAchievements = (TextView) findViewById(R.id.txtAchievements);

        Intent intent = getIntent();
        Bundle myBundle = intent.getExtras();
        account = (Account) myBundle.getSerializable("account");
        System.out.println(account.getName());
        accountName = account.getName();


        imgAvatar.setImageResource(account.getAvatar());
        txtUsername.setText(account.getName());
        txtAchievements.setText(String.valueOf(account.getnGames()));

        btnEditProfile = (LinearLayout) findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });


        ft = getSupportFragmentManager().beginTransaction();
        profileFragment = FragmentProfile.newInstance(account);
        ft.replace(R.id.profile_holder, profileFragment);

        FrameLayout frame = (FrameLayout) findViewById(R.id.profile_holder);
        frame.setOnTouchListener(new OnSwipeTouchListener(ProfileActivity.this) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();

                ft = getSupportFragmentManager().beginTransaction();
                listFriendsFragment = FragmentListFriends.newInstance(accountName);
                ft.replace(R.id.profile_holder, listFriendsFragment);
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

                ft = getSupportFragmentManager().beginTransaction();
                profileFragment = FragmentProfile.newInstance(account);
                ft.replace(R.id.profile_holder, profileFragment);
            }
        });



    }

    @Override
    public void onMsgFromFragToMain(String sender, String strValue) {
        if (sender.equals("EDIT-FLAG")) {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            Bundle bundle = new Bundle();
            // some information about player here (maybe id or player object serialization )
            intent.putExtras(bundle);
            startActivityForResult(intent, 1122);
        }
    }
}
