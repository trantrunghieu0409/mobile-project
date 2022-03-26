package com.example.mobileproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.adapter.CustomChatAdapter;
import com.example.mobileproject.models.Chat;
import com.example.mobileproject.models.Player;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.models.RoomState;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentBoxChat extends Fragment implements FragmentCallbacks {
    GameplayActivity gameplayActivity;
    Context context = null;
    ArrayList<String> message = new ArrayList<>();
    CustomChatAdapter apdater;
    ListView boxChatAnswer;
    String vocal = "";
    boolean isFirst;

    DocumentReference documentReference;
    public static FragmentBoxChat newInstance(boolean isBoxChat) {
        Bundle args = new Bundle();
        FragmentBoxChat fragment = new FragmentBoxChat();
        args.putBoolean("isBoxChat", isBoxChat);
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

        documentReference.collection("Chat").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> listMessage = value.getDocuments();
                // get latest message
                if(value.size() > 0){
                    Chat msg = listMessage.get(value.size()-1).toObject(Chat.class);
                    message.add(msg.getMsg());
                    apdater.notifyDataSetChanged();

                }
            }
        });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_box_chat = (LinearLayout) inflater.inflate(R.layout.layout_box_chat, null);

        boxChatAnswer = (ListView) layout_box_chat.findViewById(R.id.BoxChatAnswer);
        apdater = new CustomChatAdapter(message);
        boxChatAnswer.setAdapter(apdater);

        return layout_box_chat;
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {
        if(strValue.contains("`Reset`")){
            DocumentReference documentReference2 = CloudFirestore.getData("RoomState",gameplayActivity.roomID);
            documentReference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    RoomState roomState = documentSnapshot.toObject(RoomState.class);
                    vocal = roomState.getVocab();
                }
            });
        }else if(strValue.contains("`TURNFOR`")){
            String name = strValue.replace("`TURNFOR`","");
            Chat chat = new Chat("<font color=\"#0000FF\">Turn of <b>"+ name +"</b> </font>");
            documentReference.collection("Chat").document(chat.getTimestamp()).set(chat);
        }else if(strValue.contains("`ANSWER`")){
            Chat chat = new Chat("<font color=\"#0000FF\">The answer was: <b>"+ vocal +"</b></font>");
            documentReference.collection("Chat").document(chat.getTimestamp()).set(chat);
            gameplayActivity.onMsgFromFragToMain("RIGHT-FLAG", "`RIGHT`");
        }
        else{
            if(Player.checkAnswer(vocal,strValue) == 2){
                Chat chat = new Chat("<font color=\"#008000\"><b>"+ gameplayActivity.mainPlayer.getName()+"</b> hit!</font>");
                documentReference.collection("Chat").document(chat.getTimestamp()).set(chat);

                gameplayActivity.room.findPlayerAndSetPoint(gameplayActivity.mainPlayer.getName(),false,false);

                gameplayActivity.room.findPlayerAndSetPoint(gameplayActivity.currentDrawing.getName(),true,gameplayActivity.room.isFirstAnswer());
                gameplayActivity.room.setFirstAnswer(false);
                //Update
                CloudFirestore.sendData("ListofRooms", gameplayActivity.roomID, gameplayActivity.room);
            }
//            Chat chat = new Chat(strValue);
//            documentReference.collection("Chat").document(chat.getTimestamp()).set(chat);
        }

    }
}
