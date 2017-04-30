package com.example.smartalarm;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Пользователь on 25.04.2017.
 */

public class AlarmActivity extends AppCompatActivity {
    private Button button;
    private MediaPlayer mp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);
        button = (Button)findViewById(R.id.stopButton);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mp = MediaPlayer.create(getApplicationContext(),R.raw.was_wollen_wir_trinken);
        mp.setVolume(100,100);
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
            }
        });
    }

}
