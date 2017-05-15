package com.example.smartalarm;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by student2 on 08.05.17.
 */
public class SettingsActivity extends AppCompatActivity {

    private ArrayAdapter arrAdapter;
    private ListView listView;
    static Uri ringtone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        String[] arr = new String[]{"Музыка будильника","Время повтора"};
        arrAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arr);
        listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(arrAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(getApplicationContext(), position, Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0: Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
                            .putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE)
                            .putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Выбирите звонок")
                            .putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
                            .putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                        startActivityForResult(intent, 3);

                        break;
                    case 1:
                        final Dialog dialog = new Dialog(SettingsActivity.this);
                        dialog.setContentView(R.layout.time_repeat_dialog);
                        Button cancel = (Button)dialog.findViewById(R.id.cancel);
                        Button apply = (Button)dialog.findViewById(R.id.apply);
                        final EditText editText = (EditText)dialog.findViewById(R.id.editText2);
                        editText.setText(String.valueOf(AlarmActivity.repeatMinute));
                        dialog.show();
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        apply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlarmActivity aa = new AlarmActivity();
                                aa.saveRepeatMinute(Integer.parseInt(String.valueOf(editText.getText())));
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.wtf("MUSIC","ok");
        if (resultCode == Activity.RESULT_OK && requestCode == 3){
            ringtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            Log.wtf("MUSIC","okx2");
            if (ringtone != null){
                Log.wtf("MUSIC","okx3");
                AlarmActivity aa = new AlarmActivity();
                aa.saveRingtone(ringtone);
            }
        }

    }
}
