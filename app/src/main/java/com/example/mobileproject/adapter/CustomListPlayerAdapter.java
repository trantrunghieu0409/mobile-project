package com.example.mobileproject.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.models.Player;

import java.util.ArrayList;


public class CustomListPlayerAdapter extends BaseAdapter {

    ArrayList<Player> list;
    Context context;
    public CustomListPlayerAdapter(ArrayList<Player> listPlayer,Context context){
        this.list = listPlayer;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getPoint();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        Context context=parent.getContext();

        Player player = (Player) getItem(position);
        if (convertView == null) {
            row = View.inflate(parent.getContext(), R.layout.custom_list_player, null);
        }
        else row = convertView;


        ImageView iconTopScore = (ImageView) row.findViewById(R.id.iconTopScore);
        ImageView PlayerIcon=(ImageView) row.findViewById(R.id.icon);
        TextView name = (TextView) row.findViewById(R.id.name_player);
        TextView point = (TextView) row.findViewById(R.id.point_player);

        switch (position){
            case 0:
                iconTopScore.setImageResource(R.drawable.icon_winner1);
                break;
            case 1:
                iconTopScore.setImageResource(R.drawable.icon_winner2);
                break;
            case 2:
                iconTopScore.setImageResource(R.drawable.icon_winner3);
                break;
            default:
                iconTopScore.setImageResource(0);
                break;

        }

        PlayerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popupVoteKick = inflater.inflate(R.layout.popup_votekick, null);
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // tap outside to dismiss this pop up
                final PopupWindow popupWindow = new PopupWindow(popupVoteKick, width, height, focusable);

                // show a pop up
                popupWindow.showAtLocation(view, Gravity.CENTER, 0 , 0);

                final Button btnVoteKick=popupVoteKick.findViewById(R.id.votekickBtn);
                final Button btnCancel=popupVoteKick.findViewById((R.id.votekickCancelBtn));
                final TextView userTxt=popupVoteKick.findViewById(R.id.usernameTxt);

                userTxt.setText(player.getName());


                btnVoteKick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         ((GameplayActivity)context).onMsgFromFragToMain("MESS-FLAG", player.getName()+" reported`RED`");
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

        });

        PlayerIcon.setImageResource(player.getAvatar());
        name.setText(player.getName());
        point.setText(player.getPoint() + "pts");
        return row;
    }

}
