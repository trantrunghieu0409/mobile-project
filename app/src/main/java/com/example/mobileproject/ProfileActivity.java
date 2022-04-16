package com.example.mobileproject;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.adapter.CustomImageAdapter;
import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.events.OnSwipeTouchListener;
import com.example.mobileproject.fragment.FragmentListFriends;
import com.example.mobileproject.fragment.FragmentProfile;
import com.example.mobileproject.fragment.MainCallbacks;
import com.example.mobileproject.models.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class ProfileActivity extends FragmentActivity implements MainCallbacks {
    FragmentTransaction ft; FragmentProfile profileFragment; FragmentListFriends listFriendsFragment;
    LinearLayout btnChangeAvatar, btnChangePassword;
    RelativeLayout mainProfile;
    ImageView imgAvatar;
    TextView txtUsername, txtAchievements;
    AppCompatButton btnClose, btnSignOut;
    Account account;
    String accountId;
    RadioButton radioLeft, radioRight;
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
        btnSignOut=(AppCompatButton) findViewById(R.id.btnSignOut) ;
        btnChangePassword = (LinearLayout) findViewById(R.id.btnChangePassword);
        mainProfile = (RelativeLayout) findViewById(R.id.mainProfile);

        Intent intent = getIntent();
        Bundle myBundle = intent.getExtras();
        account = (Account) myBundle.getSerializable("account");
        if(account==null){
            startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
        }
        else{
            accountId = account.getAccountId();
        }

        imgAvatar.setImageResource(account.getAvatar());
        txtUsername.setText(account.getName());
        txtAchievements.setText(String.valueOf(account.getnGames()));

        btnClose = (AppCompatButton) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(view -> {
            Intent intentClose = new Intent(ProfileActivity.this, HomeActivity.class);
            Bundle bundleClose = new Bundle();
            bundleClose.putSerializable("account", account);
            intentClose.putExtras(bundleClose);
            startActivity(intentClose);
            finish();
        });

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
                listFriendsFragment = FragmentListFriends.newInstance(accountId);
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
                listFriendsFragment = FragmentListFriends.newInstance(accountId);
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
                mainProfile.setAlpha(0.5F); // 50% transparent background
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
                    mainProfile.setAlpha(1.0F); // return to normal state
                });
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                account.offline();
                Intent intent=new Intent(ProfileActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainProfile.setAlpha(0.5F); // 50% transparent background

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupChangePassword = inflater.inflate(R.layout.popup_changepassword, null);

                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                final PopupWindow popupWindow = new PopupWindow(popupChangePassword, width, height, true);

                // show a pop up
                popupWindow.showAtLocation(view, Gravity.CENTER, 0 , 0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mainProfile.setAlpha(1.0F); // return to normal state
                    }
                });

                final Button changePasswordButton = (Button) popupChangePassword.findViewById(R.id.changePasswordBtn);
                final EditText registerPassword=(EditText) popupChangePassword.findViewById(R.id.EditTextChangeNewPassword);
                final EditText registerRetypePassword=(EditText) popupChangePassword.findViewById((R.id.editTextChangePasswordRetype));
                final EditText currentPassword=(EditText) popupChangePassword.findViewById((R.id.currentPassword));
                changePasswordButton.setOnClickListener(view1 -> {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword.getText().toString());

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "User re-authenticated.");
                                    if (registerPassword.getText().toString().equals(registerRetypePassword.getText().toString())) {
                                        user.updatePassword(registerPassword.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "User password updated.");
                                                        }
                                                        Toast.makeText(ProfileActivity.this, "User password updated", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                    else{
                                        Toast.makeText(ProfileActivity.this, "Retype password do not match", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                    popupWindow.dismiss();
                    mainProfile.setAlpha(1.0F); // return to normal state
                });
            }
        });

    }

    @Override
    public void onMsgFromFragToMain(String sender, String strValue) {

    }
}
