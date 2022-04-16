package com.example.mobileproject.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mobileproject.adapter.CustomListFriendAdapter;
import com.example.mobileproject.constants.GlobalConstants;
import com.example.mobileproject.utils.CloudFirestore;
import com.example.mobileproject.utils.FriendRequestService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.type.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Account extends Player implements Serializable {

    String email;
    String hashPassword;
    int first_place;
    int second_place;
    int third_place;
    int nGames;
    int nWin;
    int nLose;
    String accountId; // player already has this ?
    String token; // player already has this?

    boolean online = false;
    long lastOnline = new Date().getTime();

    @Override
    public String getToken() {
        return token;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    ArrayList<Account>friendList;

    public Account() {

    }

    public Account(String name, int point, int avatar, String email, String hashPassword) {
        super(name, point, avatar);
        this.email = email;
        this.hashPassword = hashPassword;
        this.first_place = 0;
        this.second_place = 0;
        this.third_place = 0;
        this.nGames = 0;

    }

    public Account(String email, String hashPassword) {
        this.email = email;
        this.hashPassword = hashPassword;
        this.first_place = 0;
        this.second_place = 0;
        this.third_place = 0;
        this.nGames = 0;

    }

    public Account(String name,String email,String hashPassword,String accountId,String token)
    {
        super(name, 0, GlobalConstants.thumbnails[0]);
        this.email = email;
        this.hashPassword = null;
        this.first_place = 0;
        this.second_place = 0;
        this.third_place = 0;
        this.nGames = 0;
        this.accountId=accountId;
        this.token= token;
        friendList=new ArrayList<Account>();
    }

    public int getFirst_place() {
        return first_place;
    }

    public void setFirst_place(int first_place) {
        this.first_place = first_place;
    }

    public int getSecond_place() {
        return second_place;
    }

    public void setSecond_place(int second_place) {
        this.second_place = second_place;
    }

    public int getThird_place() {
        return third_place;
    }

    public void setThird_place(int third_place) {
        this.third_place = third_place;
    }

    public int getnGames() {
        return nGames;
    }

    public void setnGames(int nGames) {
        this.nGames = nGames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public ArrayList<Account> getFriendList(){ return friendList;}

    public int getnLose() {
        return nLose;
    }

    public int getnWin() {
        return nWin;
    }


    public static void sendDataToFirebase(Account account) {
        CloudFirestore.sendData("Account", account.getAccountId(), account).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
//                Log.e("Firebase Error", "Success");
            }
        });
    }

    public static DocumentReference getDataFromFirebase(String name) {
        DocumentReference documentReference = CloudFirestore.getData("Account", name);
        return documentReference;
    }

    public static String getCurrertAccountId() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser.getUid();
    }

    public static String getcurrentAccountEmail() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser.getEmail();
    }

    public static FirebaseUser getcurrentAccount() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser;
    }

    public static void signOut() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
    }

    public static void saveAccount(Account acc) {
        DatabaseReference AccRef = FirebaseDatabase.getInstance("https://drawguess-79bb9-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Account");
//        HashMap<String,Account> hashMap=new HashMap<>();
//        hashMap.put(acc.getAccountId(),acc);
        AccRef.child(acc.getAccountId()).setValue(acc);
    }

    public void updateAnalystAccount(Boolean[] place){
        this.nGames++;
        for(int i = 0 ; i < place.length;i++){
            if(i == 0 && place[i]){
                this.first_place++;
                this.nWin++;
                sendDataToFirebase(this);
                return;
            }
            if(i == 1 && place[i]){
                this.second_place++;
                this.nWin++;
                sendDataToFirebase(this);
                return;
            }
            if(i == 2 && place[i]){
                this.third_place++;
                this.nWin++;
                sendDataToFirebase(this);
                return;
            }
        }
        this.nLose++;
        sendDataToFirebase(this);
    }
    public static boolean isLogged (){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
                return true;
        return false;
    }

    public long getTimePlay() {
        long duration = 0;
        if (online) {
            duration = new Date().getTime() - lastOnline;
        }
        return duration;
    }

    public long getTimeOffline() {
        long duration = 0;
        if (!online) {
            Log.e("get TIME OFFLINE", email + String.valueOf(lastOnline));
            duration = new Date().getTime() - lastOnline;
        }
        return duration;
    }

    public boolean isOnline() {return online;}

    public void online() {
        online = true;
        CloudFirestore.updateField("Account", accountId, "online", true);
        CloudFirestore.updateField("Account", accountId, "lastOnline", lastOnline);
    }

    public void offline() {
        lastOnline = new Date().getTime();
        Log.d("online Time", String.valueOf(lastOnline));

        online = false;
        CloudFirestore.updateField("Account", accountId, "online", false);
        CloudFirestore.updateField("Account", accountId, "lastOnline", lastOnline);
    }

    @Override
    protected void finalize() throws Throwable {
        //if (online)
        //    offline();
        super.finalize();
    }
}


