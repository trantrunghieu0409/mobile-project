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

import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.R;


public class FragmentNotiDrawer extends Fragment implements FragmentCallbacks{

    GameplayActivity gameplayActivity;
    Context context = null;
    String Name = "";
    int Img;

    public static FragmentNotiDrawer newInstance(boolean isNotiDrawer){
        Bundle args = new Bundle();
        FragmentNotiDrawer fragmentNotiDrawer = new FragmentNotiDrawer();
        args.putBoolean("isNotiDrawer", isNotiDrawer);
        fragmentNotiDrawer.setArguments(args);
        return fragmentNotiDrawer;
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
        LinearLayout layout_notiuserdraw = null;
        layout_notiuserdraw = (LinearLayout) inflater.inflate(R.layout.layout_notiuserdraw,null);
        ImageView avatar = (ImageView) layout_notiuserdraw.findViewById(R.id.imgAvatar);
        TextView name = (TextView) layout_notiuserdraw.findViewById(R.id.name_drawing);

        name.setText(Name);
        avatar.setImageResource(Img);

        return layout_notiuserdraw;
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {
        String[] component = strValue.split("`");
        this.Name = component[0];
        this.Img = Integer.parseInt(component[1]);

    }
}