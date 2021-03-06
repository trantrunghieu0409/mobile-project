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
import com.example.mobileproject.adapter.CustomListPlayerAdapter;
import com.example.mobileproject.models.Player;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;


public class FragmentListPlayer extends Fragment{
    GameplayActivity gameplayActivity;
    Context context = null;
    ArrayList<Player> list = new ArrayList<>();
    CustomListPlayerAdapter adapter;
    DocumentReference documentReference;

    public static FragmentListPlayer newInstance(boolean isListPlayer) {
        Bundle args = new Bundle();
        FragmentListPlayer fragment = new FragmentListPlayer();
        args.putBoolean("isListPlayer", isListPlayer);
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

        //Add Data list player from database if Room have new Player
        documentReference = CloudFirestore.getData("ListofRooms",gameplayActivity.roomID);
        if(documentReference != null){
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Room room = value.toObject(Room.class);
                if(room != null){
                    if(gameplayActivity.room != null){
                        if(room.getPlayers().size() > gameplayActivity.room.getPlayers().size() && !room.isPlaying()){
                            // a new player is in
                            // call to owner to start the game
                            gameplayActivity.onMsgFromFragToMain("PLAYER-FLAG", "NEW_PLAYER");
                        }
                    }
                    gameplayActivity.room = room;
                    list.clear();
                    list.addAll(room.sortDescendingPoint());
                    adapter.notifyDataSetChanged();
                }
            }
        });
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_list_player = (LinearLayout) inflater.inflate(R.layout.layout_list_player, null);
        final ListView listViewPlayer = (ListView) layout_list_player.findViewById(R.id.list_player);

        adapter = new CustomListPlayerAdapter(list,getContext(),gameplayActivity.mainPlayer.getName());
        listViewPlayer.setAdapter(adapter);


        return layout_list_player;
    }
}
