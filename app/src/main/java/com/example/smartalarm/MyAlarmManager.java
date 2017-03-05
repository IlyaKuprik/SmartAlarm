package com.example.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Пользователь on 26.02.2017.
 */

public class MyAlarmManager extends BroadcastReceiver {
    private String name;
    private String time;
    private boolean isSmart;
    private static final String TAG="AlarmContentFragment";
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"TAG");
        wakeLock.acquire();
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
        wakeLock.release();
    }

    public void setAlarm(Context context,int triggerHour,int triggerMinute){
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,MyAlarmManager.class);
        PendingIntent pi = PendingIntent.getBroadcast(context,0,intent,0);
        if (!this.isSmart) {
            Date time = new Date();
            long interval = (triggerHour * 3600 + triggerMinute * 60) - (time.getHours() * 3600 + time.getMinutes() * 60 + time.getSeconds());
            if (interval < 0) interval += 24 * 3600;
            Log.wtf(TAG, String.valueOf(interval));
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval * 1000, pi);

            Toast.makeText(context,"Будильник сработает через: "+interval / 3600 + " ч " + (interval / 60) % 60 + " м",Toast.LENGTH_LONG).show();
        }
    }

    public void cancelAlarm(Context context){
        Intent intent = new Intent(context,MyAlarmManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,0,intent,0);
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getSmart() {
        return isSmart;
    }

    public void setSmart(boolean smart) {
        isSmart = smart;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
