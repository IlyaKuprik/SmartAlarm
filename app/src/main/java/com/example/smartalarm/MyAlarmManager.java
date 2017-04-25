package com.example.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;



public class MyAlarmManager extends BroadcastReceiver implements Serializable {
    private String name ;
    private String time = "00:00";

    private boolean checked = false;
    private boolean everyDay = false;
    private boolean smart = false;

    private int triggerHour = -1;
    private int triggerMinute = -1;
    private int alarmId = -1;



    private static final String TAG="AlarmContentFragment";
    private static final String ONE_TIME="ONE_TIME";

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,"TAG");
        wakeLock.acquire();

        Bundle extras = intent.getExtras();
        if (extras!=null && extras.getBoolean(ONE_TIME,Boolean.FALSE)){

        }
        Intent intent1 = new Intent(context,AlarmActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);

        wakeLock.release();
    }

    public void setAlarm(Context context,int triggerHour,int triggerMinute) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyAlarmManager.class);
        PendingIntent pi = PendingIntent.getBroadcast(context,alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Date time = new Date();
        this.triggerHour=triggerHour;
        this.triggerMinute=triggerMinute;

        if (triggerMinute < 10) this.time = String.valueOf(triggerHour) + ":0" +  String.valueOf(triggerMinute);
        else this.time = String.valueOf(triggerHour) + ":" +  String.valueOf(triggerMinute);

        long interval = (triggerHour * 3600 + triggerMinute * 60) - (time.getHours() * 3600 + time.getMinutes() * 60 + time.getSeconds());
        if (interval < 0) interval += 24 * 3600;
        Log.wtf(TAG, "Будильник сработает через " + String.valueOf(interval) + " секунд");
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval * 1000, pi);

        Toast.makeText(context, "Будильник сработает через: " + interval / 3600 + " ч " + (interval / 60) % 60 + " м", Toast.LENGTH_LONG).show();
        Log.wtf(TAG,"Добавлен будильник под номером " + String.valueOf(alarmId));
        checked = true;
        everyDay = false;
        smart = false;
    }

    public void setSmartAlarm(Context context,int triggerHour,int triggerMinute){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyAlarmManager.class);
        PendingIntent pi = PendingIntent.getBroadcast(context,alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Date time = new Date();

        this.triggerHour=triggerHour;
        this.triggerMinute=triggerMinute;

        if (triggerMinute < 10) this.time = String.valueOf(triggerHour) + ":0" +  String.valueOf(triggerMinute);
        else this.time = String.valueOf(triggerHour) + ":" +  String.valueOf(triggerMinute);

        long interval = (triggerHour * 3600 + triggerMinute * 60) - (time.getHours() * 3600 + time.getMinutes() * 60 + time.getSeconds());
        if (interval < 0) interval += 24 * 3600;
        long counter = 2 * 3600 + 20 * 60;
        while (interval > counter){
            counter+=2 * 3600 + 20 * 60;
        }
        counter-=2 * 3600 + 20 * 60;
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + counter * 1000 + 8 * 60 * 1000, pi);

        Toast.makeText(context, "Умный будильник сработает через: " + counter / 3600 + " ч " + (counter / 60) % 60 + " м", Toast.LENGTH_LONG).show();
        Log.wtf(TAG,"Добавлен умный будильник под номером " + String.valueOf(alarmId));
        Log.wtf(TAG, "Будильник номер " + alarmId + " сработает через " + String.valueOf(counter/3600) + " ч " + String.valueOf(counter / 60 % 60) + " м ");
        checked = true;
        everyDay = false;
        smart = true;
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, MyAlarmManager.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        Log.wtf(TAG, "Удален будильник под номером " + String.valueOf(alarmId));
        if (checked) {
            Toast.makeText(context, "Будильник отменен", Toast.LENGTH_SHORT).show();
        }
        checked = false;
    }

    public void setRepareAlarm(Context context,int triggerHour,int triggerMinute) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyAlarmManager.class);
        PendingIntent pi = PendingIntent.getBroadcast(context,alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Date time = new Date();
        this.triggerHour=triggerHour;
        this.triggerMinute=triggerMinute;

        if (triggerMinute < 10) this.time = String.valueOf(triggerHour) + ":0" +  String.valueOf(triggerMinute);
        else this.time = String.valueOf(triggerHour) + ":" +  String.valueOf(triggerMinute);

        long interval = (triggerHour * 3600 + triggerMinute * 60) - (time.getHours() * 3600 + time.getMinutes() * 60 + time.getSeconds());
        if (interval < 0) interval += 24 * 3600;
        Log.wtf(TAG, "Ежедневный будильник сработает через " + String.valueOf(interval) + " секунд");
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval * 1000, AlarmManager.INTERVAL_DAY , pi);

        Toast.makeText(context, "Ежедневный будильник сработает через: " + interval / 3600 + " ч " + (interval / 60) % 60 + " м", Toast.LENGTH_LONG).show();
        Log.wtf(TAG,"Добавлен ежедневный будильник под номером " + String.valueOf(alarmId));
        checked = true;
        everyDay = true;
        smart = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public int getTriggerHour() {
        return triggerHour;
    }

    public void setTriggerHour(int triggerHour) {
        this.triggerHour = triggerHour;
    }

    public int getTriggerMinute() {
        return triggerMinute;
    }

    public void setTriggerMinute(int triggerMinute) {
        this.triggerMinute = triggerMinute;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isEveryDay() {
        return everyDay;
    }

    public void setEveryDay(boolean everyDay) {
        this.everyDay = everyDay;
    }

    public boolean isSmart() {
        return smart;
    }

    public void setSmart(boolean smart) {
        this.smart = smart;
    }
}
