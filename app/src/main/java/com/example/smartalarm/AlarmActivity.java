package com.example.smartalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
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

    static SharedPreferences settings;
    boolean running = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences(PREFERENCES,MODE_PRIVATE);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (running){
                    Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(1000);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
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
}

