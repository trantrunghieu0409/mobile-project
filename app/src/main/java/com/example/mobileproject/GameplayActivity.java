package com.example.mobileproject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;

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

public class GameplayActivity extends Activity {
    ArrayList<Player> list = new ArrayList<>();
    ListView listViewPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add(new Player("User 1", 10));
        list.add(new Player("User 2", 5));
        setContentView(R.layout.activity_gameplay);
        listViewPlayer = (ListView) findViewById(R.id.list_player);

        CustomListPlayerAdapter adapter = new CustomListPlayerAdapter(list);
        listViewPlayer.setAdapter(adapter);
    }
}
