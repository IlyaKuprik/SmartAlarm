package com.example.smartalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Пользователь on 25.04.2017.
 */

public class AlarmActivity extends AppCompatActivity {
    private Button button;
    private Button repeatButton;
    private MediaPlayer mp;
    private TextView name;
    static int repeatMinute = 3;
    private static final String RINGTONE = "mRingtone";
    private static final String REPEAT_MINUTE = "mMinute";
    private static final String PREFERENCES = "mPreferences";

    SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.alarm_activity);
        button = (Button) findViewById(R.id.stopButton);
        repeatButton = (Button) findViewById(R.id.repeatBtn);
        name = (TextView) findViewById(R.id.alarmName);
        Bundle extras = getIntent().getExtras();
        if (settings.contains(REPEAT_MINUTE)){
            repeatMinute = settings.getInt(REPEAT_MINUTE,0);
        }
        if (extras != null)
            name.setText(extras.getString("name"));
        if (settings.contains(RINGTONE)){
            mp = MediaPlayer.create(getApplicationContext(), Uri.parse(settings.getString(RINGTONE, "")));
        }
        else {
            mp = MediaPlayer.create(getApplicationContext(),R.raw.was_wollen_wir_trinken);
        }
        mp.setVolume(100, 100);
        mp.setLooping(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mp.start();
            }
        }).start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                Toast.makeText(AlarmActivity.this, "Будильник выключен", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                Toast.makeText(AlarmActivity.this, "Будильник отложен", Toast.LENGTH_SHORT).show();
                MyAlarmManager alarm = new MyAlarmManager();
                alarm.setAlarm(getApplicationContext(), repeatMinute);
                finish();
            }
        });
    }

    public void saveRingtone(Uri uri) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(RINGTONE,uri.toString());
        editor.apply();
    }
    public void saveRepeatMinute(int repeatMinute) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(REPEAT_MINUTE, repeatMinute);
        editor.apply();
    }
}

