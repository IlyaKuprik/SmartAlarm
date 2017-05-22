package com.example.smartalarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Пользователь on 30.03.2017.
 */

public class MyDeadlineManager extends BroadcastReceiver implements Serializable{
    private StringBuilder name = new StringBuilder("Имя события");
    private StringBuilder notificationName = new StringBuilder("Имя события");

    private String date = "дата";
    private String time = "время";

    private int deadlineId = -1;

    private boolean working = false;

    private static final String TAG = "DeadlineManager";

    private ArrayList<ScrollElement> scroll;

    private int deadlineColor = 0;

    private static long NOTIFICATION_TIME;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"TAG");
        wakeLock.acquire();
        Log.e(TAG,name.toString() + " " + deadlineId);

        Intent nIntent = new Intent(context, MainActivity.class);
        PendingIntent pi =PendingIntent.getActivity(context, deadlineId, nIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentIntent(pi)
                .setSmallIcon(R.drawable.deadlines_icon)
                .setContentTitle("Напоминание")
                .setContentText(intent.getStringExtra(String.valueOf(notificationName)))
                .setColor(Color.WHITE);
        Notification notification = builder.build();
        name.delete(0,name.length());
        notification.defaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(deadlineId, builder.build());
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(2000);
        working = false;

        wakeLock.release();
    }

    public void setDeadline(Context context, long millis, String notificationName){
        NOTIFICATION_TIME = context.getSharedPreferences("mTimeSaves", Context.MODE_PRIVATE).getInt("savedTime",0) * 3600 * 1000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyDeadlineManager.class);
        intent.putExtra(this.notificationName.toString() , notificationName);
        PendingIntent pi = PendingIntent.getBroadcast(context, deadlineId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, millis - NOTIFICATION_TIME, pi);
        working = true;
    }

    public void cancelDeadline(Context context){
        Intent intent = new Intent(context, MyDeadlineManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, deadlineId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        working = false;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public StringBuilder getName() {
        return name;
    }

    public void setName(String name) {
        this.name.delete(0,this.name.length());
        this.name.append(name);
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

    public StringBuilder getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName.delete(0,this.notificationName.length());
        this.notificationName.append(notificationName);
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public ArrayList<ScrollElement> getScroll() {
        return scroll;
    }

    public void setScroll(ArrayList<ScrollElement> scroll) {
        this.scroll = scroll;
    }

    public int getDeadlineColor() {
        return deadlineColor;
    }

    public void setDeadlineColor(int deadlineColor) {
        this.deadlineColor = deadlineColor;
    }
}
