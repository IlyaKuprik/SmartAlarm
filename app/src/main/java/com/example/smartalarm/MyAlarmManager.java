package com.example.smartalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Пользователь on 26.02.2017.
 */

public class MyAlarmManager extends BroadcastReceiver {
    private String name;
    private String time;
    private boolean isSmart;

    @Override
    public void onReceive(Context context, Intent intent) {

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
