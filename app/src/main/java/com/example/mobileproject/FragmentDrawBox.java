package com.example.mobileproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentDrawBox extends Fragment implements FragmentCallbacks{
    GameplayActivity gameplayActivity;
    Context context = null;

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
        LinearLayout layout_draw_box = (LinearLayout) inflater.inflate(R.layout.layout_notiuserdraw,null);
        return layout_draw_box;

    }

        @Override
    public void onMsgFromMainToFragment(String strValue) {

    }
}
