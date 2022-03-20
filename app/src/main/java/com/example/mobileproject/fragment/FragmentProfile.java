package com.example.mobileproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.ProfileActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.models.Account;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class FragmentProfile extends Fragment implements FragmentCallbacks {
    ProfileActivity profileActivity; Context context = null;
    TextView txtGames, txtWin, txtLose, txtFirst, txtSecond, txtThird;
    Account account;
    public static FragmentProfile newInstance(Account account) {
        FragmentProfile fragment = new FragmentProfile();
        fragment.account = account;
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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout_profile = (LinearLayout) inflater.inflate(R.layout.layout_profile, null);

        txtGames = (TextView) layout_profile.findViewById(R.id.txtGames);
        txtWin = (TextView) layout_profile.findViewById(R.id.txtWin);
        txtLose = (TextView) layout_profile.findViewById(R.id.txtLose);
        txtFirst = (TextView) layout_profile.findViewById(R.id.txtFirst);
        txtSecond = (TextView) layout_profile.findViewById(R.id.txtSecond);
        txtThird = (TextView) layout_profile.findViewById(R.id.txtThird);

        txtGames.setText(String.valueOf(account.getnGames()));
        int nWin = account.getFirst_place() + account.getSecond_place()
                + account.getThird_place();
        txtWin.setText(String.valueOf(nWin));
        txtLose.setText(String.valueOf(account.getnGames() - nWin));
        txtFirst.setText(String.valueOf(account.getFirst_place()));
        txtSecond.setText(String.valueOf(account.getSecond_place()));
        txtThird.setText(String.valueOf(account.getThird_place()));

        return layout_profile;
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {

    }
}
