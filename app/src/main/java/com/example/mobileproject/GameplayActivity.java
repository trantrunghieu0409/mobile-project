package com.example.mobileproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;


import com.example.mobileproject.custom_adapter.CustomChatPopupApdater;

import java.util.ArrayList;
import java.util.Objects;


public class GameplayActivity extends FragmentActivity implements MainCallbacks {
    FragmentTransaction ft;
    com.example.mobileproject.fragment.FragmentDrawBox FragmentDrawBox;
    com.example.mobileproject.fragment.FragmentListPlayer FragmentListPlayer;
    com.example.mobileproject.fragment.FragmentChatInput FragmentChatInput;
    com.example.mobileproject.fragment.FragmentBoxChat FragmentBoxChat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        ft = getSupportFragmentManager().beginTransaction();

        FragmentDrawBox = FragmentDrawBox.newInstance(true);
        ft.replace(R.id.holder_box_draw, FragmentDrawBox);


        FragmentListPlayer = FragmentListPlayer.newInstance(true);
        ft.replace(R.id.holder_list_player, FragmentListPlayer);

        FragmentChatInput = FragmentChatInput.newInstance(true);
        ft.replace(R.id.holder_chat_input, FragmentChatInput);

        FragmentBoxChat = FragmentBoxChat.newInstance(true);
        ft.replace(R.id.holder_chat_box, FragmentBoxChat);

        ft.commit();
    }

    @Override
    public void onMsgFromFragToMain(String sender, String strValue) {
        if (sender.equals("MESS-FLAG")) {
            FragmentBoxChat.onMsgFromMainToFragment(strValue);
        }

    }
}
