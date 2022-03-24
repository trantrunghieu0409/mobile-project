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
import com.example.mobileproject.utils.CloudFirestore;
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
        Chat chat = new Chat(strValue);
        documentReference.collection("Chat").document(chat.getTimestamp()).set(chat);
    }
}
