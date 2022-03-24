package com.example.mobileproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.adapter.CustomImageAdapter;
import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.events.OnSwipeTouchListener;
import com.example.mobileproject.fragment.FragmentListFriends;
import com.example.mobileproject.fragment.FragmentProfile;
import com.example.mobileproject.fragment.MainCallbacks;
import com.example.mobileproject.models.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

public class ProfileActivity extends FragmentActivity implements MainCallbacks {
    FragmentTransaction ft; FragmentProfile profileFragment; FragmentListFriends listFriendsFragment;
    LinearLayout btnChangeAvatar, btnChangePassword;
    ImageView imgAvatar;
    TextView txtUsername, txtAchievements;
    LinearLayout btnEditProfile;
    ImageButton btnClose;
    Account account;
    String accountName;
    RadioButton radioLeft, radioRight;
    Button btnSignOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtAchievements = (TextView) findViewById(R.id.txtAchievements);
        radioLeft = (RadioButton) findViewById(R.id.radioLeft);
        radioRight = (RadioButton) findViewById(R.id.radioRight);
        btnSignOut=(Button)findViewById(R.id.btnSignOut) ;


        Intent intent = getIntent();
        Bundle myBundle = intent.getExtras();
        account = (Account) myBundle.getSerializable("account");
        accountName = account.getName();


        imgAvatar.setImageResource(account.getAvatar());
        txtUsername.setText(account.getName());
        txtAchievements.setText(String.valueOf(account.getnGames()));

//        btnEditProfile = (LinearLayout) findViewById(R.id.btnEditProfile);
//        btnEditProfile.setOnClickListener(view -> {
//            Intent intent1 = new Intent(ProfileActivity.this, EditProfileActivity.class);
//            intent1.putExtra("account", account);
//            startActivityForResult(intent1, 110);
//        });

        btnClose = (ImageButton) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> { finish(); });

        ft = getSupportFragmentManager().beginTransaction();
        profileFragment = FragmentProfile.newInstance(account);
        ft.replace(R.id.profile_holder, profileFragment); ft.commit();

        FrameLayout frame = (FrameLayout) findViewById(R.id.profile_holder);

        radioLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft = getSupportFragmentManager().beginTransaction();
                profileFragment = FragmentProfile.newInstance(account);
                ft.replace(R.id.profile_holder, profileFragment);
                ft.commit();

                radioLeft.setChecked(true);
                radioRight.setChecked(false);
            }
        });

        radioRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft = getSupportFragmentManager().beginTransaction();
                listFriendsFragment = FragmentListFriends.newInstance(accountName);
                ft.replace(R.id.profile_holder, listFriendsFragment);
                ft.commit();

                radioRight.setChecked(true);
                radioLeft.setChecked(false);
            }
        });

        frame.setOnTouchListener(new OnSwipeTouchListener(ProfileActivity.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeRight();

                ft = getSupportFragmentManager().beginTransaction();
                listFriendsFragment = FragmentListFriends.newInstance(accountName);
                ft.replace(R.id.profile_holder, listFriendsFragment);
                ft.commit();

                radioRight.setChecked(true);
                radioLeft.setChecked(false);
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeLeft();

                ft = getSupportFragmentManager().beginTransaction();
                profileFragment = FragmentProfile.newInstance(account);
                ft.replace(R.id.profile_holder, profileFragment);
                ft.commit();

                radioLeft.setChecked(true);
                radioRight.setChecked(false);
            }
        });


        imgAvatar.setOnClickListener(new View.OnClickListener() {
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
                gridViewAvatar.setAdapter(new CustomImageAdapter(ProfileActivity.this, GlobalConstants.thumbnails));
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
                final Button btnOK = (Button) popupAvatarList.findViewById(R.id.btnOk);
                btnOK.setOnClickListener(view1 -> {
                    if (chosenPos[0] != -1) {
                        int avatarSrc = GlobalConstants.thumbnails[chosenPos[0]];
                        imgAvatar.setImageResource(avatarSrc);
                        account.setAvatar(avatarSrc);
                        Account.sendDataToFirebase(account);
                    }
                    else {
                        // do nothing
                    }
                    popupWindow.dismiss();
                });
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
                startActivity(intent);
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
