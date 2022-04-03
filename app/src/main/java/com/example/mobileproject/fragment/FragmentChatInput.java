package com.example.mobileproject.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.adapter.CustomChatPopupApdater;
import com.example.mobileproject.models.Chat;
import com.example.mobileproject.models.RoomState;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class FragmentChatInput extends Fragment implements FragmentCallbacks {
    GameplayActivity gameplayActivity;
    Context context = null;
    ImageButton btnReport;
    ImageButton btnAudio;
    ImageButton btnPopUpInfo;
    ImageButton btnPopUpChat;
    ImageButton btnSendAnswer;
    EditText editTextAnswer;
    ImageView popUpChat;
    TextView popUpChatText;

    boolean audio;
    boolean report;
    int popUpNoti = 0;

    DocumentReference documentReference;
    ArrayList<Chat> messages = new ArrayList<>();
    CustomChatPopupApdater customChatPopupApdater;

    public static FragmentChatInput newInstance(boolean isChatInput) {
        Bundle args = new Bundle();
        FragmentChatInput fragment = new FragmentChatInput();
        args.putBoolean("isChatInput", isChatInput);
        fragment.setArguments(args);
        return fragment;
    }

    private void updatePopupNoti(){
        if(this.popUpNoti < 1){
            popUpChat.setImageResource(0);
            popUpChatText.setText("");
        }
        else{
            popUpChat.setImageResource(R.drawable.icon_offline);
            popUpChatText.setText(String.valueOf(popUpNoti));
        }
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
        customChatPopupApdater = new CustomChatPopupApdater(messages,gameplayActivity.mainPlayer);
        if(documentReference != null){
        documentReference.collection("ChatPopUp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> listMessage = value.getDocuments();
                // get latest message
                if(value.size() > 1){
                    Chat msg = listMessage.get(value.size()-1).toObject(Chat.class);
                    messages.add(msg);
                    popUpNoti++;
                    customChatPopupApdater.notifyDataSetChanged();
                    updatePopupNoti();
                }
            }
        });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_chat_input = (LinearLayout) inflater.inflate(R.layout.layout_chat_input, null);


        btnReport = (ImageButton) layout_chat_input.findViewById(R.id.btnReport);
        btnAudio = (ImageButton) layout_chat_input.findViewById(R.id.btnAudio);
        btnPopUpInfo = (ImageButton) layout_chat_input.findViewById(R.id.btnPopUpInfo);
        btnPopUpChat = (ImageButton) layout_chat_input.findViewById(R.id.btnPopUpChat);
        btnSendAnswer = (ImageButton) layout_chat_input.findViewById(R.id.btnSendAnswer);
        popUpChat = (ImageView) layout_chat_input.findViewById(R.id.statusPopupChat);
        popUpChatText = (TextView) layout_chat_input.findViewById(R.id.statusPopupChatText);

        editTextAnswer = (EditText) layout_chat_input.findViewById(R.id.editTextAnswer);


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

                    final Button btnVoteKick=popupVoteKick.findViewById(R.id.votekickBtn);
                    final Button btnCancel=popupVoteKick.findViewById((R.id.votekickCancelBtn));
                    final TextView nameUser = popupVoteKick.findViewById(R.id.usernameTxt);
                    final ImageView avatarUser = popupVoteKick.findViewById(R.id.imgAvatar);

                    nameUser.setText(gameplayActivity.currentDrawing.getName());
                    avatarUser.setImageResource(gameplayActivity.currentDrawing.getAvatar());

                    btnVoteKick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String mess = "<font color=\"#FF0000\"> <b>"+ gameplayActivity.mainPlayer.getName() +"</b> reported </font>";
                            gameplayActivity.onMsgFromFragToMain("REPORT-FLAG", mess);
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
                popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


                ListView boxchat = popupChat.findViewById(R.id.box_chat_popup);
                ImageView btnClose = popupChat.findViewById(R.id.btnClosePopUpChat);
                final ImageButton btnSendChat =  (ImageButton) popupChat.findViewById(R.id.btnSendChat);

                final EditText editTextChat = (EditText) popupChat.findViewById(R.id.editTextChat);

                editTextChat.requestFocus();
                boxchat.setAdapter(customChatPopupApdater);


                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                        popUpNoti = 0;
                        updatePopupNoti();
                    }
                });

                btnSendChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mess = String.valueOf(editTextChat.getText());
                        editTextChat.setText("");
                        customChatPopupApdater.notifyDataSetChanged();
                        Chat chat = new Chat(gameplayActivity.mainPlayer.getName(),mess,gameplayActivity.mainPlayer.getAvatar());
                        documentReference.collection("ChatPopUp").document(chat.getTimestamp()).set(chat);
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
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        popUpNoti = 0;
                        updatePopupNoti();
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
                gameplayActivity.onMsgFromFragToMain("MESS-FLAG",  mess);
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

    @Override
    public void onMsgFromMainToFragment(String strValue) {
        if(strValue.contains("`Reset`")){
//            editTextAnswer.setFocusableInTouchMode(true);
            editTextAnswer.setEnabled(true);
        }
        else if(strValue.contains("`RIGHT`")){
//            editTextAnswer.setFocusable(false);
            editTextAnswer.setEnabled(false);
        }
        else{
            Chat chat = new Chat(strValue);
            documentReference.collection("ChatPopUp").document(chat.getTimestamp()).set(chat);
        }
    }
}
