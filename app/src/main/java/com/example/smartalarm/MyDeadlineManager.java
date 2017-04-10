package com.example.smartalarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Пользователь on 30.03.2017.
 */

public class MyDeadlineManager extends BroadcastReceiver implements Serializable{
    private String name = "Имя события";
    private String date = "дата";
    private String time = "время";

    private int deadlineId = -1;

    private static final String TAG = "DeadlineManager";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"TAG");
        wakeLock.acquire();

        Intent nIntent = new Intent(context, MainActivity.class);
        PendingIntent pi =PendingIntent.getActivity(context, deadlineId, nIntent, PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(pi)
                .setSmallIcon(R.drawable.small_notification_icon)
                .setContentTitle("Напоминание")
                .setContentText(name);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(deadlineId, builder.build());
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        Log.e(TAG, name);

        wakeLock.release();
    }

    public void setDeadline(Context context, long millis){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyDeadlineManager.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, deadlineId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, millis, pi);
        Log.e(TAG,String.valueOf((millis - System.currentTimeMillis()) / 1000));

    }

    public void cancelDeadline(Context context){
        Intent intent = new Intent(context, MyDeadlineManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, deadlineId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDeadlineId() {
        return deadlineId;
    }

    public void setDeadlineId(int deadlineId) {
        this.deadlineId = deadlineId;
    }
}
