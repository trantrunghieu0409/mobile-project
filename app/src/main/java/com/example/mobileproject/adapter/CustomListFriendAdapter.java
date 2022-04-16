package com.example.mobileproject.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileproject.R;
import com.example.mobileproject.models.Account;
import com.example.mobileproject.models.Player;
import com.example.mobileproject.utils.FormatDate;

import java.util.ArrayList;


public class CustomListFriendAdapter extends BaseAdapter {
    Context context;
    TextView username, txtOnline;
    ImageView avatar, iconStatus;
    ImageButton btnUnfriend;

    ArrayList<Account> friendList;

    public CustomListFriendAdapter(ArrayList<Account> friendList, Context context){
        this.friendList = friendList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int i) {
        return friendList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return friendList.get(i).getPoint();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            row = View.inflate(parent.getContext(), R.layout.custom_list_friend, null);
        }
        else
            row = convertView;

        Account friend = friendList.get(position);

        username = (TextView) row.findViewById(R.id.txtUsername);
        avatar = (ImageView) row.findViewById(R.id.imgAvatar);
        btnUnfriend = (ImageButton) row.findViewById(R.id.btnUnfriend);

        iconStatus = (ImageView) row.findViewById(R.id.iconStatus);
        txtOnline = (TextView) row.findViewById(R.id.txtOnline);


        username.setText(friend.getName());
        avatar.setImageResource(friend.getAvatar());

        if (friend.isOnline()) {
            iconStatus.setImageResource(R.drawable.icon_online);
            txtOnline.setText("Active now");
        }
        else {
            iconStatus.setImageResource(R.drawable.icon_offline);
            txtOnline.setText(FormatDate.formatDuration(friend.getTimeOffline()));
        }

        btnUnfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return row;
    }

}
