package com.example.mobileproject.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileproject.DrawActivity;
import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.MainActivity;
import com.example.mobileproject.R;
import com.example.mobileproject.models.Room;
import com.example.mobileproject.models.RoomState;
import com.example.mobileproject.utils.CloudFirestore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class FragmentGetDrawing extends Fragment implements FragmentCallbacks {
    GameplayActivity gameplayActivity; TextView txtHint;
    Context context = null;
    private ImageView i;
    private Bitmap b;
    private RoomState room;
    Thread syncThread;
    public String roomID;
    public static FragmentGetDrawing newInstance(String roomID) {
        Bundle args = new Bundle();
        FragmentGetDrawing fragment = new FragmentGetDrawing();
        fragment.roomID = roomID;
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
        LinearLayout layout_getdrawing;
        layout_getdrawing = (LinearLayout) inflater.inflate(R.layout.layout_getdrawing,null);

        txtHint = (TextView) layout_getdrawing.findViewById(R.id.txtHint);
        txtHint.setText(roomID);
        i = (ImageView) layout_getdrawing.findViewById(R.id.imageView);
        b = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        i.post(() -> {
            b = Bitmap.createBitmap(i.getWidth(), i.getHeight(), Bitmap.Config.ARGB_8888);
            i.setImageBitmap(b);
        });

        syncThread = new Thread(() -> {
            try {
                while (true) {
                    DocumentReference docRef = CloudFirestore.getData("RoomState", roomID);
                    if (docRef != null) {
                        docRef.get().addOnSuccessListener(documentSnapshot -> {
                            room = documentSnapshot.toObject(RoomState.class);

                            if (room != null) {
                                if (room.getImgBitmap() != null) { // if not draw anything
                                    // show drawing
                                    b = CloudFirestore.decodeBitmap(room.getImgBitmap());
                                    i.setImageBitmap(b);
                                }

                                // show hint
                                if (room.isShowHint())
                                    txtHint.setText(room.getHint());
                            }
                        });
                    }
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        syncThread.start();
        return layout_getdrawing;
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {
        if (strValue.equals("END-FLAG") && syncThread.isAlive()) {
            syncThread.interrupt();
        }
    }
}
