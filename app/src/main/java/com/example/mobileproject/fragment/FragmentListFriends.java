package com.example.mobileproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.ProfileActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.adapter.CustomListFriendAdapter;
import com.example.mobileproject.models.Account;

import java.util.ArrayList;

public class FragmentListFriends extends Fragment implements FragmentCallbacks {
    ProfileActivity profileActivity; Context context = null;
    static String accountName;
    CustomListFriendAdapter adapter;
    ListView listViewFriends;

    ArrayList<Account> listFriends;


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
        LinearLayout layout_list_friends = (LinearLayout) inflater.inflate(R.layout.layout_list_friends, null);
        listViewFriends = (ListView)  layout_list_friends.findViewById(R.id.list_friends);

        adapter = new CustomListFriendAdapter(listFriends, getContext());
        listViewFriends.setAdapter(adapter);

        return layout_list_friends;
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {

    }
}
