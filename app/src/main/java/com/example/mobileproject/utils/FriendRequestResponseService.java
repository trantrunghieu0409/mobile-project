//package com.example.mobileproject.utils;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.ClipData;
//import android.content.ClipboardManager;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.media.Ringtone;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//import android.os.IBinder;
//import android.os.Vibrator;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.window.SplashScreen;
//
//import androidx.annotation.RequiresApi;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//
//
//import com.example.mobileproject.GameplayActivity;
//import com.example.mobileproject.HomeActivity;
//import com.example.mobileproject.ProfileActivity;
//import com.example.mobileproject.R;
//import com.example.mobileproject.models.Account;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.RemoteMessage;
//import android.app.Activity;
//
//import java.io.Serializable;
//
//
//import java.io.Serializable;
//
//public class FriendRequestResponseService extends com.google.firebase.messaging.FirebaseMessagingService {
//    NotificationManager mNotificationManager;
//    static private FirebaseAuth mAuth;
////
////    public static String getToken(Context context) {
////        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
////    }
////
////    public static void createToken(Context context) {
////        mAuth = FirebaseAuth.getInstance();
////        if (mAuth.getCurrentUser() != null) {
////            FirebaseMessaging.getInstance().getToken()
////                    .addOnCompleteListener(task -> {
////                        if (task.isSuccessful() && task.getResult() != null) {
////                            Log.e("newToken", task.getResult());
////                            context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", task.getResult()).apply();
////                        }
////                    });
////        }
////        else{
////            Log.e("newToken","empty");
////            context.getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", "empty").apply();
////        }
////    }
//
//
////    @Override
////    public void onNewToken(String s) {
////        super.onNewToken(s);
////        Log.e("newToken", s);
////        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
////    }
//
//
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
//
//
//// playing audio and vibration when user se request
//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//        r.play();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            r.setLooping(false);
//        }
//
//        // vibration
//        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        long[] pattern = {100, 300, 300, 300};
//        v.vibrate(pattern, -1);
//
//
//        int resourceImage = getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName());
//
//
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////            builder.setSmallIcon(R.drawable.icontrans);
//            builder.setSmallIcon(R.drawable.icon);
//        } else {
////            builder.setSmallIcon(R.drawable.icon_kritikar);
//            builder.setSmallIcon(R.drawable.icon);
//        }
//
//
//
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra("no",false);
//        intent.addFlags (Intent.FLAG_ACTIVITY_SINGLE_TOP
//                |Intent. FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        builder.setContentTitle(remoteMessage.getNotification().getTitle());
//        builder.setContentText(remoteMessage.getNotification().getBody());
//        builder.setContentIntent(pendingIntent);
//        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
//        builder.setAutoCancel(true);
//        builder.setPriority(Notification.PRIORITY_MAX);
//
//        mNotificationManager =
//                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            String channelId = "channel_id";
//            NotificationChannel channel = new NotificationChannel(
//                    channelId,
//                    "Channel human readable title",
//                    NotificationManager.IMPORTANCE_HIGH);
//            mNotificationManager.createNotificationChannel(channel);
//            builder.setChannelId(channelId);
//        }
//
//
//
//// notificationId is a unique int for each notification that you must define
//        mNotificationManager.notify(100, builder.build());
//
//
//    }
//}
