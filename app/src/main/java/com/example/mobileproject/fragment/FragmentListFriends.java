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

import com.example.mobileproject.ProfileActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.adapter.CustomListFriendAdapter;
import com.example.mobileproject.models.Account;
import com.example.mobileproject.models.Chat;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class FragmentListFriends extends Fragment implements FragmentCallbacks {
    ProfileActivity profileActivity; Context context = null;
    static String accountId;
    CustomListFriendAdapter adapter;
    ListView listViewFriends;
    static ArrayList<Account> listFriends;
    static String roomId;
    DocumentReference documentReference;



    public static FragmentListFriends newInstance(String id) {
        FragmentListFriends fragment = new FragmentListFriends();
        accountId = id;
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try{
            context = getActivity();
            profileActivity = (ProfileActivity) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("Activity must implement callbacks");
        }
//        documentReference = CloudFirestore.getData("Account", Account.getCurrertAccountId());
//        if(documentReference != null){
//            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    assert documentSnapshot != null;
//
//                    room = documentSnapshot.toObject(Account.class);
//
//
//                    }
//
//                }
//            });

        // get friends info from firebase here

//
//        Account acc = new Account("user", 0, R.drawable.avatar_batman, "email@gmail.com", "password");
//        listFriends.add(acc);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        LinearLayout layout_list_friends = (LinearLayout) inflater.inflate(R.layout.layout_list_friends, null);
        listViewFriends = (ListView)  layout_list_friends.findViewById(R.id.list_friends);
        listFriends=new ArrayList<>();
        adapter = new CustomListFriendAdapter(listFriends, getContext());
        listViewFriends.setAdapter(adapter);
        DatabaseReference reqRef = FirebaseDatabase.getInstance("https://drawguess-79bb9-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Requests").child(accountId);
        DatabaseReference accRef = FirebaseDatabase.getInstance("https://drawguess-79bb9-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Account");
//        ArrayList<Account> pendingList=new ArrayList<>(100);
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
        return layout_list_friends;
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {

    }
}
