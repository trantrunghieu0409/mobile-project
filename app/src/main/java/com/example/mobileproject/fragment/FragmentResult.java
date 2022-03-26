package com.example.mobileproject.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.utils.CloudFirestore;

public class FragmentResult extends Fragment implements FragmentCallbacks{

    GameplayActivity gameplayActivity;
    Context context = null;

    public static FragmentResult newInstance(String result){
        Bundle args = new Bundle();
        FragmentResult fragmentResult = new FragmentResult();
        args.putString("Result", result);
        fragmentResult.setArguments(args);
        return fragmentResult;
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
        LinearLayout layout_result = null;
        layout_result = (LinearLayout) inflater.inflate(R.layout.layout_result,null);
        return layout_result;
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {

    }
}
