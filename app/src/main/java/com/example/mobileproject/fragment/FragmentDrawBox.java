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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileproject.DrawActivity;
import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.MainActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.adapter.CustomListFriendAdapter;
import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.models.Account;
import com.example.mobileproject.models.RoomState;
import com.example.mobileproject.models.Topic;
import com.example.mobileproject.utils.CloudFirestore;
import com.example.mobileproject.utils.FriendRequestService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.example.mobileproject.models.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FragmentDrawBox extends Fragment implements FragmentCallbacks {
    GameplayActivity gameplayActivity;
    Context context = null;
    Button buttonStart;
    RoomState roomState;
    RadioButton  radioRight;
    FragmentTransaction ft;
    FragmentListFriends listFriendsFragment;


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
            if(gameplayActivity.mainPlayer.getName().equals(gameplayActivity.room.getOwnerUsername()) && !gameplayActivity.room.isPlaying()){
                layout_draw_box = (LinearLayout) inflater.inflate(R.layout.layout_waitstart,null);
                popupInvitation(gameplayActivity.mainPlayer.getAccountId());
                buttonStart = (Button) layout_draw_box.findViewById(R.id.btnStart);
                if(gameplayActivity.room.getPlayers().size() == 1){
                    buttonStart.setEnabled(false);
                }
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
        if(strValue.equals("NEW_PLAYER") && gameplayActivity.mainPlayer.getName().equals(gameplayActivity.room.getOwnerUsername())){
            buttonStart.setEnabled(true);
        }
        if(strValue.equals("CLOSE") && gameplayActivity.mainPlayer.getName().equals(gameplayActivity.room.getOwnerUsername())){
            buttonStart.setEnabled(false);
        }
    }
    public void popupInvitation(String playerId){
        AlertDialog.Builder builder = new AlertDialog.Builder(gameplayActivity);
        LayoutInflater layoutInflater = getLayoutInflater();

        //this is custom dialog
        //custom_popup_dialog contains textview only
        View customView = layoutInflater.inflate(R.layout.popup_shareinvite, null);
        // reference the textview of custom_popup_dialog
        Button buttonDrawTurn = customView.findViewById(R.id.copyButton);
        TextView txtInvitation = customView.findViewById(R.id.txtInvitation);
        txtInvitation.setText(gameplayActivity.roomID);
        radioRight = (RadioButton)customView.findViewById(R.id.shareRadioRight);
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
        radioRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupFriendInvitation(playerId);
//                radioRight.setChecked(true);
//                radioLeft.setChecked(false);
            }
        });
        alert.show();
    }

    public void popupFriendInvitation(String playerId){
        AlertDialog.Builder builder = new AlertDialog.Builder(gameplayActivity);
        LayoutInflater layoutInflater = getLayoutInflater();

        //this is custom dialog
        //custom_popup_dialog contains textview only
        if (playerId==null)
        {
            Toast.makeText(gameplayActivity, "Only logged in user can invite friends!", Toast.LENGTH_LONG);
        }
        else{
            LinearLayout layout_list_friends = (LinearLayout) layoutInflater.inflate(R.layout.layout_list_friends, null, false);
            if(layout_list_friends.getParent() != null) {
                ((ViewGroup)layout_list_friends.getParent()).removeView(layout_list_friends); // <- fix
            }
            ListView listViewFriends = (ListView) layout_list_friends.findViewById(R.id.list_friends);
            ArrayList<Account>  listFriends=new ArrayList<>();
            CustomListFriendAdapter adapter = new CustomListFriendAdapter(listFriends, getContext());

            listViewFriends.setAdapter(adapter);
            DatabaseReference reqRef = FirebaseDatabase.getInstance("https://drawguess-79bb9-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Requests").child(playerId);
            DatabaseReference accRef = FirebaseDatabase.getInstance("https://drawguess-79bb9-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Account");

            ArrayList<String> idList=new ArrayList<>();
            reqRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String status=dataSnapshot.child("status").getValue(String.class);
                        String reqId=dataSnapshot.getKey();
                        if (status.equals("pending")) {
                            accRef.child(reqId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Account a = snapshot.getValue(Account.class);
                                    listFriends.add(a);
                                    idList.add(a.getAccountId());
                                    adapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            final int[] chosenPos = {-1};
            Button button = new Button(gameplayActivity);
            button.setText("Invite");
            layout_list_friends.addView(button);


            listViewFriends.setOnItemClickListener((adapterView, view12, i, l) -> {
                if (chosenPos[0] != -1) {
                    View v = listViewFriends.getChildAt(chosenPos[0]);
                    v.setBackgroundColor(124333); // initial background color of gridview
                }
                chosenPos[0] = i;
                view12.setBackgroundColor(R.drawable.red_background_border);
            });


            button.setOnClickListener(view1 -> {
                if (chosenPos[0] != -1) {
                    Log.d("sender id",idList.get(chosenPos[0]) );
                    FriendRequestService.sendMessage(listViewFriends.getContext(), gameplayActivity, "You have invitation to Guess Draw",gameplayActivity.roomID, idList.get(chosenPos[0]));
                    Toast.makeText((GameplayActivity) context, "Invitation sent successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText((GameplayActivity) context, "Cannot send message", Toast.LENGTH_SHORT).show();
                }
//            builder.dismiss();
            });
            builder.setView(layout_list_friends);
        }

        AlertDialog alert = builder.create();
        alert.show();
    }

}
