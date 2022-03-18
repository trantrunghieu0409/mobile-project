package com.example.mobileproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.custom_adapter.RecyclerChatAdapter;
import com.example.mobileproject.models.Chat;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentBoxChat extends Fragment implements FragmentCallbacks {
    GameplayActivity gameplayActivity;
    Context context = null;
    ArrayList<String> message = new ArrayList<>();
    RecyclerChatAdapter adapterRecycler;
    RecyclerView boxChatAnswer;


    DocumentReference documentReference ;
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
        documentReference.collection("Chat").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                message.clear();
                for (QueryDocumentSnapshot document : value) {
                    Chat msg = document.toObject(Chat.class);
                    message.add(msg.getNameUser() + ": " + msg.getMsg());
                    boxChatAnswer.scrollToPosition(Objects.requireNonNull(boxChatAnswer.getAdapter()).getItemCount() - 1);
                    adapterRecycler.notifyDataSetChanged();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_box_chat = (LinearLayout) inflater.inflate(R.layout.layout_box_chat, null);
        boxChatAnswer = (RecyclerView) layout_box_chat.findViewById(R.id.BoxChatAnswer);
        LinearLayoutManager manager = new LinearLayoutManager(context);
//        manager.setReverseLayout(true);
        boxChatAnswer.setLayoutManager(manager);
        boxChatAnswer.setHasFixedSize(true);
        adapterRecycler = new RecyclerChatAdapter(message);
        boxChatAnswer.setAdapter(adapterRecycler);

        return layout_box_chat;
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {
        String[] msg = strValue.split(":");
        System.out.println(msg.length);
        documentReference.collection("Chat").add(new Chat(msg[0],msg[1]));
    }
}
