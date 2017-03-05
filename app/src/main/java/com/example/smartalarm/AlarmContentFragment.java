package com.example.smartalarm;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

/**
 * Created by Пользователь on 26.02.2017.
 */

public class AlarmContentFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.item_alarm,null);
        RecyclerView recyclerView=(RecyclerView)inflater.inflate(R.layout.recycler_view,container,false);
        ContentAdapter contentAdapter=new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(contentAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView time;
        Button settingsBtn;
        TimePicker timePicker;

        MyAlarmManager alarm;

        Button timeButton;

        TextView settingsTime;
        EditText alarmEditText;
        CheckBox smartBtn;
        CheckBox vibrateBtn;
        Button applySettingsBtn;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent, final Context context) {
            super(inflater.inflate(R.layout.item_alarm,parent,false));
            name=(TextView)itemView.findViewById(R.id.name);
            time=(TextView)itemView.findViewById(R.id.time);
            alarm=new MyAlarmManager();

            final Dialog timeDialog=new Dialog(context);
            timeDialog.setContentView(R.layout.time_picker);

            timePicker=(TimePicker)timeDialog.findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);

            timeButton=(Button)timeDialog.findViewById(R.id.applyTimeButton);
            timeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alarm.setAlarm(context,timePicker.getCurrentHour(),timePicker.getCurrentMinute());
                    time.setText(timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute());
                    timeDialog.dismiss();
                }
            });

            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeDialog.show();
                }
            });

            final Dialog settingsDialog=new Dialog(context);
            settingsDialog.setContentView(R.layout.settings_dialog);

            settingsTime=(TextView)settingsDialog.findViewById(R.id.settingsTime);
            alarmEditText=(EditText)settingsDialog.findViewById(R.id.editAlarmName);
            smartBtn=(CheckBox)settingsDialog.findViewById(R.id.smartBtn);

            settingsBtn=(Button)itemView.findViewById(R.id.alarmSettingsBtn);

            settingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settingsDialog.show();
                }
            });

            applySettingsBtn=(Button)settingsDialog.findViewById(R.id.applySettingsBtn);
            applySettingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (alarmEditText!=null) name.setText(String.valueOf(alarmEditText.getText()));
                    settingsDialog.dismiss();
                }
            });

            }

    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        private ArrayList<MyAlarmManager> alarms=new ArrayList<>();

        public ContentAdapter(Context context){
            Resources resources=context.getResources();
            alarms.add(new MyAlarmManager());
            alarms.add(new MyAlarmManager());
            for (int i = 0; i <alarms.size() ; i++) {
                alarms.get(i).setTime(resources.getString(R.string.default_time));
                alarms.get(i).setName(resources.getString(R.string.zero));
                alarms.get(i).setAlarmId(i);
            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()),parent,parent.getContext());
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.name.setText(alarms.get(position).getName());
            holder.time.setText(alarms.get(position).getTime());
        }

        @Override
        public int getItemCount() {
            return alarms.size();
        }
    }
}
