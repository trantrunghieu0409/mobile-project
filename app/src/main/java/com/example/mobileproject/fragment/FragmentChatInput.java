package com.example.mobileproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.adapter.CustomChatPopupApdater;
import com.example.mobileproject.models.Chat;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class FragmentChatInput extends Fragment {
    GameplayActivity gameplayActivity;
    Context context = null;
    boolean audio;
    boolean report;
    DocumentReference documentReference;
    ArrayList<String> messages = new ArrayList<>();
    CustomChatPopupApdater customChatPopupApdater;

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
        documentReference = CloudFirestore.getData("ListofRooms",gameplayActivity.roomID);
        if(documentReference != null){

        documentReference.collection("ChatPopUp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> listMessage = value.getDocuments();
                // get latest message
                if(value.size() > 0){
                    Chat msg = listMessage.get(value.size()-1).toObject(Chat.class);
                    messages.add(msg.getMsg());
                    customChatPopupApdater.notifyDataSetChanged();
                }
            }
        });
        }
        messages.add("Hello");
        messages.add("Hi");
        messages.add("HiHi");
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
//                if(!report){
//                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View popupVoteKick = inflater.inflate(R.layout.popup_votekick, null);
//                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
//                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    boolean focusable = true; // tap outside to dismiss this pop up
//                    final PopupWindow popupWindow = new PopupWindow(popupVoteKick, width, height, focusable);
//
//                    // show a pop up
//                    popupWindow.showAtLocation(view, Gravity.CENTER, 0 , 0);
//
//                    final Button btnVoteKick=popupVoteKick.findViewById(R.id.votekickBtn);
//                    final Button btnCancel=popupVoteKick.findViewById((R.id.votekickCancelBtn));
//
//                    btnVoteKick.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            String mess = "<font color=\"#FF0000\"> <b> User </b> reported </font>";
//                            gameplayActivity.onMsgFromFragToMain("MESS-FLAG", mess);
//                            btnReport.setEnabled(false);
//                            popupWindow.dismiss();
//                        }
//                    });
//                    btnCancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            popupWindow.dismiss();
//                        }
//                    });
//
//                }
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

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupInfo = inflater.inflate(R.layout.popup_info, null);
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupInfo, width, height, true);

                popupWindow.showAtLocation(view,Gravity.CENTER, 0 , 0);
            }
        });

        btnPopUpChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupChat = inflater.inflate(R.layout.popup_chat, null);
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                final PopupWindow popupWindow = new PopupWindow(popupChat, width, height, true);



                ListView boxchat = popupChat.findViewById(R.id.box_chat_popup);
                ImageView btnClose = popupChat.findViewById(R.id.btnClosePopUpChat);
                final ImageButton btnSendChat =  (ImageButton) popupChat.findViewById(R.id.btnSendChat);

                final EditText editTextChat = (EditText) popupChat.findViewById(R.id.editTextChat);

                editTextChat.requestFocus();

                customChatPopupApdater = new CustomChatPopupApdater(messages);
                boxchat.setAdapter(customChatPopupApdater);


                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

                btnSendChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mess = String.valueOf(editTextChat.getText());
                        editTextChat.setText("");
//                        messages.add(mess);
//                        customChatPopupApdater.notifyDataSetChanged();
                        Chat chat = new Chat(mess);
                        documentReference.collection("Chat2").document(chat.getTimestamp()).set(chat);
                    }
                });

                editTextChat.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                            btnSendChat.callOnClick();
                            return true;
                        }
                        return false;
                    }
                });

                popupWindow.showAtLocation(view,Gravity.CENTER, 0 , 0);


            }
        });

        btnSendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mess = String.valueOf(editTextAnswer.getText());
                editTextAnswer.setText("");
                gameplayActivity.onMsgFromFragToMain("MESS-FLAG", gameplayActivity.userName + ": " + mess);
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
