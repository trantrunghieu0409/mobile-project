package com.example.mobileproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.models.Player;
import java.util.ArrayList;

import com.mikhaellopez.circularimageview.CircularImageView;

public class FragmentGameOver extends Fragment implements FragmentCallbacks{

    GameplayActivity gameplayActivity;
    Context context = null;
    String result;

    public static FragmentGameOver newInstance(boolean isGameOver) {
        Bundle args = new Bundle();
        FragmentGameOver fragment = new FragmentGameOver();
        args.putBoolean("isGameOver", isGameOver);
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
        RelativeLayout layout_gameover = null;
        ArrayList<Player> topPlayer = gameplayActivity.room.Top3Player();
        if(topPlayer.size() == 1){
            layout_gameover = (RelativeLayout) inflater.inflate(R.layout.layout_gameover_1,null);
            TextView nameWinner1 = (TextView) layout_gameover.findViewById(R.id.nameWinner1);
            CircularImageView imgWinner1 = (CircularImageView) layout_gameover.findViewById(R.id.imgWinner1);
            nameWinner1.setText(topPlayer.get(0).getName());
            imgWinner1.setImageResource(topPlayer.get(0).getAvatar());
        }
        else{
            layout_gameover = (RelativeLayout) inflater.inflate(R.layout.layout_gameover,null);
            TextView nameWinner1 = (TextView) layout_gameover.findViewById(R.id.nameWinner1);
            TextView nameWinner2 = (TextView) layout_gameover.findViewById(R.id.nameWinner2);
            TextView nameWinner3 = (TextView) layout_gameover.findViewById(R.id.nameWinner3);
            CircularImageView imgWinner1 = (CircularImageView) layout_gameover.findViewById(R.id.imgWinner1);
            CircularImageView imgWinner2 = (CircularImageView) layout_gameover.findViewById(R.id.imgWinner2);
            CircularImageView imgWinner3 = (CircularImageView) layout_gameover.findViewById(R.id.imgWinner3);

            nameWinner1.setText(topPlayer.get(0).getName());
            nameWinner2.setText(topPlayer.get(1).getName());
            nameWinner3.setText(topPlayer.get(2).getName());
            imgWinner1.setImageResource(topPlayer.get(0).getAvatar());
            imgWinner2.setImageResource(topPlayer.get(1).getAvatar());
            imgWinner3.setImageResource(topPlayer.get(2).getAvatar());
        }
        return layout_gameover;
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {
        this.result = strValue;
    }
}
