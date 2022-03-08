package com.example.mobileproject;

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

import java.util.ArrayList;

class Player {
    private String name;
    private int point;
    public Player(String name, int point){
        this.name = name;
        this.point = point;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getPoint() {
        return point;
    }

    public String getName() {
        return name;
    }
}

public class FragmentListPlayer extends Fragment {
    GameplayActivity gameplayActivity;
    Context context = null;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_list_player = (LinearLayout) inflater.inflate(R.layout.layout_list_player, null);


        ArrayList<Player> list = new ArrayList<>();
        final ListView listViewPlayer = (ListView) layout_list_player.findViewById(R.id.list_player);;


        list.add(new Player("User 1", 10));
        list.add(new Player("User 2", 5));
        CustomListPlayerAdapter adapter = new CustomListPlayerAdapter(list);
        listViewPlayer.setAdapter(adapter);


        return layout_list_player;
    }


}
