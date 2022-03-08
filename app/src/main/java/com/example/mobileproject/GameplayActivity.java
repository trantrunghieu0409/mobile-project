package com.example.mobileproject;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;



public class GameplayActivity extends FragmentActivity implements MainCallbacks {
    FragmentTransaction ft;
    FragmentListPlayer FragmentListPlayer;
    FragmentChatInput FragmentChatInput;
    FragmentBoxChat FragmentBoxChat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        ft = getSupportFragmentManager().beginTransaction();
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
        if (sender.equals("INFO-FLAG")) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_info);
            dialog.show();
        }

        if (sender.equals("CHAT-FLAG")) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.popup_chat);

            ImageView btnClose = dialog.findViewById(R.id.btnClosePopUpChat);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        if (sender.equals("MESS-FLAG")) {
            FragmentBoxChat.onMsgFromMainToFragment(strValue);
        }

    }
}
