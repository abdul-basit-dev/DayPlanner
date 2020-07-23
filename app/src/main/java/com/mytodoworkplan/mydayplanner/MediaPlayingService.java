package com.mytodoworkplan.mydayplanner;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public class MediaPlayingService extends Service {

    public static final String URI_BASE = MediaPlayingService.class.getName() + ".";
    public static final String ACTION_DISMISS = URI_BASE + "ACTION_DISMISS";
    public static final String DISMISSING_ACTION = URI_BASE + "DISMISSING_ACTION";
    String title,message;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        //player = MediaPlayer.create(getApplicationContext(),som);
        title = intent.getStringExtra("title");
        message = intent.getStringExtra("message");

        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone toque = RingtoneManager.getRingtone(getApplicationContext(), som);
        toque.stop();


        String action = intent.getAction();
        if (ACTION_DISMISS.equals(action)) {

            cancelAlarm();

        } else if (DISMISSING_ACTION.equals(action)) {


            cancelAlarm();
            snoozeAlarm();
        }
        return START_NOT_STICKY;
    }

    public void snoozeAlarm(){
        Intent intent = new Intent(this, MyAlarmServices.class);
        intent.putExtra("title",title);
        intent.putExtra("message",message);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        Date date = calendar.getTime();
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);

       // Toast.makeText(this, "Snoozed", Toast.LENGTH_SHORT).show();
    }
    public void cancelAlarm() {
        NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(NOTIFICATION_SERVICE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(), MyAlarmServices.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 1, myIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        alarmManager.cancel(pendingIntent);
        //Toast.makeText(this, "Dismissed", Toast.LENGTH_SHORT).show();
        alarmManager.cancel(pendingIntent);
        notificationManager.cancel(1);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}