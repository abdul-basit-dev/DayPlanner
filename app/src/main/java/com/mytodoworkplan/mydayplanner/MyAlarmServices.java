package com.mytodoworkplan.mydayplanner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyAlarmServices extends BroadcastReceiver {

    String notificationID = "MyChannel_999";


    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        //Get id and message from intent
//        int noitiID = intent.getIntExtra("notificationID", 0);
//        int noitiID = (int)System.currentTimeMillis();
        int noitiID = intent.getIntExtra("notificationID", 0);
        String message = intent.getStringExtra("message");
        String title = intent.getStringExtra("title");
       // Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();

        //On tapping notification ,open activity
        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }
        //prepare notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id));
        builder.setSmallIcon(R.drawable.ic_baseline_av_timer_24)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(false)
                .setContentIntent(contentIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setChannelId(context.getString(R.string.default_notification_channel_id));

        notificationManager.notify(noitiID, builder.build());

        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setColor(context.getResources().getColor(R.color.colorGolden));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.ic_baseline_av_timer_24);
            builder.setColor(context.getResources().getColor(R.color.colorGolden));
        } else {
            builder.setSmallIcon(R.drawable.ic_baseline_av_timer_24);
        }
        //wl.release();

    }

    //set up notification Channel

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "AppsNotificationChannel";
        String adminChannelDescription = "Daily Notification";
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(notificationID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }

    }
}

