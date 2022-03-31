package com.example.mobileproject.utils;

//import static com.example.mobileproject.constants.GlobalConstants.YES_ACTION;
//import static com.example.mobileproject.constants.GlobalConstants.NO_ACTION;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mobileproject.GameplayActivity;
import com.example.mobileproject.LoginActivity;
import com.example.mobileproject.MainActivity;
import com.example.mobileproject.ProfileActivity;

//public class NotificationReceiver extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
////        // TODO Auto-generated method stub
////        String action = intent.getAction();
////        if (YES_ACTION.equals(action)) {
////            FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
////                    FriendRequestService.getToken(context),
////                    "Friend request",
////                    "Your request is accepted", (context), MainActivity.this);
////            notificationsSender.SendNotifications();
////        } else if (NO_ACTION.equals(action)) {
////            Toast.makeText(context, "STOP CALLED", Toast.LENGTH_SHORT).show();
////        }
//    }
//}