package com.example.mobileproject;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


public class FragmentChatInput extends Fragment {
    GameplayActivity gameplayActivity;
    Context context = null;
    boolean audio;
    boolean report;

    public static FragmentChatInput newInstance(boolean isChatInput) {
        Bundle args = new Bundle();
        FragmentChatInput fragment = new FragmentChatInput();
        args.putBoolean("isChatInput", isChatInput);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            context = getActivity();
            gameplayActivity = (GameplayActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("Activity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_chat_input = (LinearLayout) inflater.inflate(R.layout.layout_chat_input, null);


        final ImageButton btnReport = (ImageButton) layout_chat_input.findViewById(R.id.btnReport);
        final ImageButton btnAudio = (ImageButton) layout_chat_input.findViewById(R.id.btnAudio);
        final ImageButton btnPopUpInfo = (ImageButton) layout_chat_input.findViewById(R.id.btnPopUpInfo);
        final ImageButton btnPopUpChat = (ImageButton) layout_chat_input.findViewById(R.id.btnPopUpChat);
        final ImageButton btnSendAnswer = (ImageButton) layout_chat_input.findViewById(R.id.btnSendAnswer);

        final EditText editTextAnswer = (EditText) layout_chat_input.findViewById(R.id.editTextAnswer);


        audio = true;
        report = false;
        btnReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(!report){
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupVoteKick = inflater.inflate(R.layout.popup_votekick, null);
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // tap outside to dismiss this pop up
                    final PopupWindow popupWindow = new PopupWindow(popupVoteKick, width, height, focusable);

                    // show a pop up
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0 , 0);
//
                    final Button btnVoteKick=popupVoteKick.findViewById(R.id.votekickBtn);
                    final Button btnCancel=popupVoteKick.findViewById((R.id.votekickCancelBtn));

                    btnVoteKick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            gameplayActivity.onMsgFromFragToMain("MESS-FLAG", "User reported`RED`");
                            btnReport.setEnabled(false);
                            popupWindow.dismiss();
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });

                }
            }
        });

        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(audio){
                    btnAudio.setImageResource(R.drawable.ic_baseline_volume_off_24);
                    audio = false;
                }
                else{
                    btnAudio.setImageResource(R.drawable.ic_baseline_volume_up_24);
                    audio = true;
                }
            }
        });

        btnPopUpInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameplayActivity.onMsgFromFragToMain("INFO-FLAG", "info_popup");
            }
        });

        btnPopUpChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameplayActivity.onMsgFromFragToMain("CHAT-FLAG", "chat_popup");
            }
        });

        btnSendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mess = String.valueOf(editTextAnswer.getText());
                editTextAnswer.setText("");
                gameplayActivity.onMsgFromFragToMain("MESS-FLAG", mess);
            }
        });

        editTextAnswer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    btnSendAnswer.callOnClick();
                    return true;
                }
                return false;
            }
        });

        return layout_chat_input;
    }
}
