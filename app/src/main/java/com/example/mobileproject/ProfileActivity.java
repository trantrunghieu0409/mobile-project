package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.fragment.FragmentProfile;
import com.example.mobileproject.fragment.MainCallbacks;

public class ProfileActivity extends FragmentActivity implements MainCallbacks {
    FragmentTransaction ft; FragmentProfile fragmentProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // create a new Profile Fragment
        ft = getSupportFragmentManager().beginTransaction();
        fragmentProfile = FragmentProfile.newInstance(true);
        ft.replace(R.id.profile_holder, fragmentProfile); ft.commit();
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
