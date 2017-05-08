package com.example.smartalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Пользователь on 25.04.2017.
 */

public class AlarmActivity extends AppCompatActivity {
    private Button button;
    private Button repeatButton;
    static Uri ringtone;
    private MediaPlayer mp;
    private TextView name;
    static int repeatMinute = 1;
    private static final String APP_PREFERENCES = "MySettings";
    private static final String FILE_NAME = "savedRingtone.txt";

    private SharedPreferences settings;

    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(ringtone,getApplicationContext());
        Log.wtf("lol","пауза");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);
        if (loadFromFile(getApplicationContext()) != null){
            Log.wtf("lol","Вск ок");
            ringtone = loadFromFile(getApplicationContext());
        }
        settings = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
        button = (Button)findViewById(R.id.stopButton);
        repeatButton = (Button) findViewById(R.id.repeatBtn);
        name = (TextView) findViewById(R.id.alarmName);
        Bundle extras = getIntent().getExtras();
        if (extras!= null)
            name.setText(extras.getString("name"));
        mp = MediaPlayer.create(getApplicationContext(),ringtone);
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

    public int getRepeatMinute() {
        return repeatMinute;
    }

    public void setRepeatMinute(int repeatMinute) {
        this.repeatMinute = repeatMinute;
    }
     static void saveToFile(Uri uri, Context context){
        try {
            Log.d("FILE_DIR", context.getFilesDir().toString());
            FileOutputStream fos = new FileOutputStream(context.getFilesDir().toString() + "/" + FILE_NAME);
            ObjectOutputStream oos= new ObjectOutputStream(fos);
            oos.writeObject(uri);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static Uri loadFromFile(Context context){
        try {
            FileInputStream fis = new FileInputStream(context.getFilesDir().toString() + "/" + FILE_NAME);
            ObjectInputStream ois= new ObjectInputStream(fis);
            Uri uri=(Uri)ois.readObject();
            ois.close();
            return uri;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(loadFromFile(getApplicationContext())!=null)
            ringtone = loadFromFile(getApplicationContext());
    }
}
