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
import com.example.mobileproject.custom_adapter.CustomListPlayerAdapter;
import com.example.mobileproject.draw_config.Config;
import com.example.mobileproject.models.Player;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentListPlayer extends Fragment implements FragmentCallbacks {
    GameplayActivity gameplayActivity;
    Context context = null;

    ArrayList<Player> list;
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
        list = new ArrayList<>();

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
        LinearLayout layout_list_player = (LinearLayout) inflater.inflate(R.layout.layout_list_player, null);


        DocumentReference documentReference = CloudFirestore.getData("ListofRooms",gameplayActivity.roomID);

        list = gameplayActivity.room.getPlayers();
        final ListView listViewPlayer = (ListView) layout_list_player.findViewById(R.id.list_player);
        CustomListPlayerAdapter adapter = new CustomListPlayerAdapter(list,getContext());

//        list.add(new Player("User 4", 20, Config.Avatars[0]));
//        list.add(new Player("User 3", 15, Config.Avatars[1]));
//        list.add(new Player("User 1", 10, Config.Avatars[2]));
//        list.add(new Player("User 2", 5, Config.Avatars[0]));

        listViewPlayer.setAdapter(adapter);
        return layout_list_player;
    }


    @Override
    public void onMsgFromMainToFragment(String strValue) {
        gameplayActivity.roomID = strValue;
    }
}
