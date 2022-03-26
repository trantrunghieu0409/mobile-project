package com.example.mobileproject.fragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.DrawActivity;
import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.MainActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.models.RoomState;
import com.example.mobileproject.models.Topic;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.mobileproject.models.Room;


public class FragmentDrawBox extends Fragment implements FragmentCallbacks {
    GameplayActivity gameplayActivity;
    Context context = null;
    Button buttonStart;
    RoomState roomState;

    public static FragmentDrawBox newInstance(boolean isDrawBox) {
        Bundle args = new Bundle();
        FragmentDrawBox fragment = new FragmentDrawBox();
        args.putBoolean("isDrawBox", isDrawBox);
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_draw_box = null;
            if(gameplayActivity.mainPlayer.getName().equals(gameplayActivity.room.getOwnerUsername()) && gameplayActivity.room.isPlaying() == false){
                layout_draw_box = (LinearLayout) inflater.inflate(R.layout.layout_waitstart,null);
                popupInvitation();
                buttonStart = (Button) layout_draw_box.findViewById(R.id.btnStart);
                buttonStart.setEnabled(false);
                buttonStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent drawIntent = new Intent(gameplayActivity, DrawActivity.class);
//                        drawIntent.putExtra("Timeout", gameplayActivity.MAX_PROGRESS);
//                        drawIntent.putExtra("roomID", gameplayActivity.roomID);
                        Room newRoom = gameplayActivity.room.deepcopy();
                        newRoom.setDrawer(0);
                        CloudFirestore.sendData("ListofRooms", gameplayActivity.roomID, newRoom);
                    }
                });
            }
            else {
                layout_draw_box = (LinearLayout) inflater.inflate(R.layout.layout_waiting,null);
            }
        return layout_draw_box;
    }

        @Override
    public void onMsgFromMainToFragment(String strValue) {
        if(strValue.equals("NEW_PLAYER")){
            buttonStart.setEnabled(true);
        }
    }
    public void popupInvitation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(gameplayActivity);
        LayoutInflater layoutInflater = getLayoutInflater();

        //this is custom dialog
        //custom_popup_dialog contains textview only
        View customView = layoutInflater.inflate(R.layout.popup_shareinvite, null);
        // reference the textview of custom_popup_dialog
        Button buttonDrawTurn = customView.findViewById(R.id.copyButton);
        TextView txtInvitation = customView.findViewById(R.id.txtInvitation);
        txtInvitation.setText(gameplayActivity.roomID);
        builder.setView(customView);
        AlertDialog alert = builder.create();
        buttonDrawTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) gameplayActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("invitation", txtInvitation.getText().toString());
                clipboard.setPrimaryClip(clip);
                alert.dismiss();
                Toast.makeText(gameplayActivity, "Copied to clipboard", Toast.LENGTH_LONG);
            }
        });
        alert.show();
    }
}
