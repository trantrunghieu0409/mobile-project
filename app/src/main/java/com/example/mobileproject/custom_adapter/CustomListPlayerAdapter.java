package com.example.mobileproject.custom_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileproject.R;
import com.example.mobileproject.module.Player;

import java.util.ArrayList;


public class CustomListPlayerAdapter extends BaseAdapter {
    Context context;
    ArrayList<Player> list;
    public CustomListPlayerAdapter(ArrayList<Player> listPlayer){
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
        ImageView iconTopScore = (ImageView) row.findViewById(R.id.iconTopScore);
        switch (position){
            case 0:
                iconTopScore.setImageResource(R.drawable.icon_winner1);
                break;
            case 1:
                iconTopScore.setImageResource(R.drawable.icon_winner2);
                break;
            case 2:
                iconTopScore.setImageResource(R.drawable.icon_winner3);
                break;
            default:
                iconTopScore.setImageResource(0);
                break;

        }
        TextView name = (TextView) row.findViewById(R.id.name_player);
        TextView point = (TextView) row.findViewById(R.id.point_player);
        Player player = (Player) getItem(position);
        name.setText(player.getName());
        point.setText(player.getPoint() + "pts");
        return row;
    }

}
