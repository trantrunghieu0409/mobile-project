package com.example.mobileproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.ProfileActivity;
import com.example.mobileproject.R;

public class FragmentProfile extends Fragment implements FragmentCallbacks {
    ProfileActivity profileActivity; Context context = null;
    public static FragmentProfile newInstance(boolean isEditable) {
        Bundle args = new Bundle();
        FragmentProfile fragment = new FragmentProfile();
        args.putBoolean("isEditable", isEditable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            context = getActivity();
            profileActivity = (ProfileActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("Activity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_profile = (LinearLayout) inflater.inflate(R.layout.layout_profile, null);

        final ImageView imgAvatar = (ImageView) layout_profile.findViewById(R.id.imgAvatar);
        final TextView txtUsername = (TextView) layout_profile.findViewById(R.id.txtUsername);
        final TextView txtAchievements = (TextView) layout_profile.findViewById(R.id.txtAchievements);

        final LinearLayout btnEditProfile = (LinearLayout) layout_profile.findViewById(R.id.btnEditProfile);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileActivity.onMsgFromFragToMain("EDIT-FLAG", "Edit");
            }
        });

        return layout_profile;
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {

    }
}
