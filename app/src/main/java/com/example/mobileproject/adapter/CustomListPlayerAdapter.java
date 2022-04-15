package com.example.mobileproject.adapter;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.models.Account;
import com.example.mobileproject.models.Player;
import com.example.mobileproject.utils.FcmNotificationsSender;
import com.example.mobileproject.utils.FriendRequestService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class CustomListPlayerAdapter extends BaseAdapter {

    ArrayList<Player> list;
    Context context;
    String username;
    String currentState;
    DatabaseReference requestRef,friendRef,receiveRef;

    public CustomListPlayerAdapter(ArrayList<Player> listPlayer, Context context, String username) {
        this.list = listPlayer;
        this.context = context;
        this.username = username;
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
        View row = View.inflate(parent.getContext(), R.layout.custom_list_player, null);
        Context context = parent.getContext();


        Player player = (Player) getItem(position);

        requestRef = FirebaseDatabase.getInstance("https://drawguess-79bb9-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Requests").child(player.getAccountId());

        if(!Account.isLogged())
        {
            receiveRef=null;
        }
        else{
            receiveRef = FirebaseDatabase.getInstance("https://drawguess-79bb9-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Requests").child(Account.getCurrertAccountId());

            requestRef.child(Account.getCurrertAccountId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentState=snapshot.child("status").getValue(String.class);
                    if (currentState == null) currentState = "nothing_happen";

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        friendRef = FirebaseDatabase.getInstance("https://drawguess-79bb9-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference().child("Friends");
        currentState = "nothing_happend";
//        if (convertView == null) {
//            row = View.inflate(parent.getContext(), R.layout.custom_list_player, null);
//        }
//        else row = convertView;

        ImageView iconStatus = (ImageView) row.findViewById(R.id.turnStatus);
        ImageView iconTopScore = (ImageView) row.findViewById(R.id.iconTopScore);
        ImageView PlayerIcon = (ImageView) row.findViewById(R.id.icon);
        TextView name = (TextView) row.findViewById(R.id.name_player);
        TextView point = (TextView) row.findViewById(R.id.point_player);

        switch (player.getStatus()) {
            case 1:
                iconStatus.setImageResource(R.drawable.check);
                break;
            case 2:
                iconStatus.setImageResource(R.drawable.pencil2);
                break;
            default:
                iconStatus.setImageResource(0);
                break;
        }
        switch (position) {
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
                if(receiveRef==null || player.getAccountId().equals(Account.getCurrertAccountId()))
                {
                    //do nothing
                }
                else
                {
                    LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View popupVoteKick = inflater.inflate(R.layout.popup_votekick, null);
                    int width = LinearLayout.LayoutParams.MATCH_PARENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // tap outside to dismiss this pop up
                    final PopupWindow popupWindow = new PopupWindow(popupVoteKick, width, height, focusable);

                    // show a pop up
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                    final Button btnVoteKick = popupVoteKick.findViewById(R.id.votekickBtn);
                    final Button btnCancel = popupVoteKick.findViewById((R.id.votekickCancelBtn));
                    final Button btnFriendRequest = popupVoteKick.findViewById(R.id.friendRequestBtn);
                    final TextView userTxt = popupVoteKick.findViewById(R.id.usernameTxt);

                    userTxt.setText(player.getName());


                    btnVoteKick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((GameplayActivity) context).onMsgFromFragToMain("MESS-FLAG", player.getName() + " reported`RED`");
                            popupWindow.dismiss();
                        }
                    });

//                    Toast.makeText((GameplayActivity) context, currentState, Toast.LENGTH_SHORT).show();
                    btnFriendRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

//                        String token=player.getToken();
//                        if(token.equals("empty")){
//                            Toast.makeText((GameplayActivity) context,"Can not send request to guest player",Toast.LENGTH_LONG).show();
//                        }
//                        else{
//                            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
//                                    token,
//                                    "Friend request",
//                                    "A friend request is sent from "+player.getName()+"\nDo you want to accept it",(GameplayActivity)context,(GameplayActivity) context );
//                            notificationsSender.SendNotifications();
//                        }

//                            Toast.makeText((GameplayActivity) context, currentState, Toast.LENGTH_SHORT).show();

                            if (Account.getcurrentAccount() == null || player.getAccountId() == null) {
                                Toast.makeText((GameplayActivity) context, "Only logged in user can send friend request", Toast.LENGTH_SHORT).show();
                            } else if (player.getAccountId().equals("empty")) {
                                Toast.makeText((GameplayActivity) context, "Can not send request to guest user", Toast.LENGTH_SHORT).show();
                            } else {
                                if (currentState.equals("nothing_happen")) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("status", "pending");
                                    requestRef.child(Account.getCurrertAccountId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText((GameplayActivity) context, "You have sent Friend request", Toast.LENGTH_SHORT).show();
                                                currentState = "pending";
                                                btnFriendRequest.setText("Cancel request");
                                                receiveRef.child(player.getAccountId()).updateChildren(hashMap);


                                            } else {
                                                Toast.makeText((GameplayActivity) context, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                                }
                                if (currentState.equals("pending") || currentState.equals("I_sent_decline")) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("status", "nothing_happen");
                                    requestRef.child(Account.getCurrertAccountId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText((GameplayActivity) context, "You have cancelled friend request", Toast.LENGTH_SHORT).show();
                                                btnFriendRequest.setText("Send Friend Request");
                                                currentState = "nothing_happen";
                                                receiveRef.child(player.getAccountId()).updateChildren(hashMap);
                                            } else {
                                                Toast.makeText((GameplayActivity) context, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

//                        if (currentState.equals("receive_decline") ){
//                            HashMap hashMap = new HashMap();
//                            hashMap.put("status", "pending");
//                            requestRef.child(Account.getCurrertAccountId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
//                                @Override
//                                public void onComplete(@NonNull Task task) {
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText((GameplayActivity) context, "Your request has been send", Toast.LENGTH_SHORT).show();
//                                        btnFriendRequest.setText("Cancel Friend Request");
//                                        currentState="Nothing_happen";
//                                    } else {
//                                        Toast.makeText((GameplayActivity) context, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }

                                if (currentState.equals("friend")) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("status", "nothing_happen");
                                    requestRef.child(Account.getCurrertAccountId()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText((GameplayActivity) context, "Your request has been accepted", Toast.LENGTH_SHORT).show();
                                                btnFriendRequest.setText("Cancel Friend Request");
                                                currentState = "nothing_happen";
                                                receiveRef.child(player.getAccountId()).updateChildren(hashMap);

                                            } else {
                                                Toast.makeText((GameplayActivity) context, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                        }
                    });


                }
            }
        });
        PlayerIcon.setImageResource(player.getAvatar());
        name.setText(player.getName());
        if (player.getName().equals(username)) {
            name.setTextColor(Color.RED);
        }
        point.setText(player.getPoint() + "pts");
        return row;

    }
}