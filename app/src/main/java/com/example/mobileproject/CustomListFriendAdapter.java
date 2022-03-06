package com.example.mobileproject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomListFriendAdapter extends BaseAdapter {
    Context context;
    ArrayList<Player> list;
    public CustomListFriendAdapter(ArrayList<Player> listPlayer){
        this.list = listPlayer;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getPoint();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            row = View.inflate(parent.getContext(), R.layout.custom_list_player, null);
        }
        else row = convertView;
        TextView name = (TextView) row.findViewById(R.id.name_player);
        TextView point = (TextView) row.findViewById(R.id.point_player);
        Player player = (Player) getItem(position);
        name.setText(player.getName());
        return row;
    }

}
