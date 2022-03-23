package com.example.mobileproject.adapter;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mobileproject.R;

import java.util.ArrayList;

public class CustomChatAdapter extends BaseAdapter {
    private ArrayList<String> messages;

    public CustomChatAdapter(ArrayList<String> messages){
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        if (convertView == null) {
            row = View.inflate(parent.getContext(), R.layout.custom_item_chat, null);
        }
        else row = convertView;

        TextView mess = (TextView) row.findViewById(R.id.item_messengerAnswer);

        mess.setText(Html.fromHtml(messages.get(position)));
        return row;
    }
}
