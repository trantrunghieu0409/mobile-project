package com.example.mobileproject.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileproject.R;
import com.example.mobileproject.models.Chat;
import com.example.mobileproject.models.Player;

import java.util.ArrayList;

public class CustomChatPopupApdater extends BaseAdapter {
    ArrayList<Chat> list;
    Player player;

    public CustomChatPopupApdater(ArrayList<Chat> list,Player player){
        this.list = list;
        this.player = player;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if(list.get(position).getNameUser().equals(player.getName())){
            row = View.inflate(parent.getContext(), R.layout.custom_item_chat_popup2, null);
        }else if(list.get(position).getNameUser().isEmpty()){
            row = View.inflate(parent.getContext(), R.layout.custom_item_chat_popup3, null);
        }
        else{
            row = View.inflate(parent.getContext(), R.layout.custom_item_chat_popup, null);
        }



        TextView mess = (TextView) row.findViewById(R.id.item_text_chat);
        TextView name = (TextView) row.findViewById(R.id.item_name_text_chat);
        ImageView icon = (ImageView) row.findViewById(R.id.icon_popup_chat);
        if(name != null){
            name.setText(list.get(position).getNameUser());
        }
        if(icon != null){
            icon.setImageResource(list.get(position).getAvatar());
        }
        mess.setText(list.get(position).getMsg());
        return row;
    }
}
