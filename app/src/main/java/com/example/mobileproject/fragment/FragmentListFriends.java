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

public class FragmentListFriends extends Fragment implements FragmentCallbacks {
    ProfileActivity profileActivity; Context context = null;
    static String accountName;

    public static FragmentListFriends newInstance(String name) {
        FragmentListFriends fragment = new FragmentListFriends();
        accountName = name;
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
        LinearLayout layout_listfriends = (LinearLayout) inflater.inflate(R.layout.layout_listfriends, null);


        return layout_listfriends;
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {

    }
}
