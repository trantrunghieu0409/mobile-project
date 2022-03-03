package com.example.mobileproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerChatAdapter extends RecyclerView.Adapter<RecyclerChatAdapter.DataViewHolder> {
    private ArrayList<String> messages;

    public RecyclerChatAdapter(ArrayList<String> message){
        this.messages = message;
    }
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_chat,parent,false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        String message = messages.get(position);
        if(message.contains("`RED`")){
            message = message.replace("`RED`","");
            holder.mess.setTextColor(Color.RED);
        }
        else holder.mess.setTextColor(Color.BLACK);
        holder.mess.setText(message);
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder{
        public TextView mess;
        public DataViewHolder(View itemView){
            super(itemView);

            mess = (TextView) itemView.findViewById(R.id.item_messengerAnswer);
        }

    }
}
