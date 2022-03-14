package com.example.mobileproject.custom_adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mobileproject.R;

import java.util.ArrayList;

public class CustomChatPopupApdater extends BaseAdapter {
    ArrayList<String> list;

    public CustomChatPopupApdater(ArrayList<String> list){
        this.list = list;
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
        if (convertView == null) {
            if (position == 1) {
                row = View.inflate(parent.getContext(), R.layout.custom_item_chat_popup2, null);
            } else {
                row = View.inflate(parent.getContext(), R.layout.custom_item_chat_popup, null);
            }
        }
        else row = convertView;
        TextView mess = (TextView) row.findViewById(R.id.item_text_chat);
        System.out.println(list);
        mess.setText(list.get(position));


        return row;
    }
}
