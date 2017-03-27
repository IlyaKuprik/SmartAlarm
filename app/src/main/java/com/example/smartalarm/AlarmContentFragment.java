package com.example.smartalarm;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Layout;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AlarmContentFragment extends Fragment {

    static ContentAdapter contentAdapter;

    public static final String SAVED_LIST="alarmList.txt";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.item_alarm,null);
        RecyclerView recyclerView=(RecyclerView)inflater.inflate(R.layout.recycler_view,container,false);
        contentAdapter=new ContentAdapter();
        recyclerView.setAdapter(contentAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView time;
        TextView settingsTime;

        EditText alarmEditText;

        Button timeButton;
        Button settingsBtn;
        Button applySettingsBtn;
        Button deleteAlarmBtn;

        CheckBox smartBtn;
        Switch switchBtn;

        TimePicker timePicker;

        MyAlarmManager alarm;

        Dialog settingsDialog;
        Dialog timeDialog;

        public ViewHolder(View view) {
            super(view);
            final Context context=view.getContext();
            name=(TextView)itemView.findViewById(R.id.name);
            time=(TextView)itemView.findViewById(R.id.time);
            switchBtn=(Switch)itemView.findViewById(R.id.alarmSwitch);
            alarm=new MyAlarmManager();

            timeDialog=new Dialog(context);
            timeDialog.setContentView(R.layout.time_picker);

            timePicker=(TimePicker)timeDialog.findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);

            timeButton=(Button)timeDialog.findViewById(R.id.applyTimeButton);

            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeDialog.show();
                }
            });

            settingsDialog=new Dialog(context);
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

            deleteAlarmBtn=(Button)settingsDialog.findViewById(R.id.deleteAlarmBtn);

            applySettingsBtn=(Button)settingsDialog.findViewById(R.id.applySettingsBtn);
            applySettingsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (alarmEditText!=null) name.setText(String.valueOf(alarmEditText.getText()));
                    settingsDialog.dismiss();
                }
            });

            settingsTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timeDialog.show();
                }
            });
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder>{

        protected static ArrayList<MyAlarmManager> alarms=new ArrayList<>();

        View view;

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder,final int position) {
            for (int i = 0; i <alarms.size() ; i++) {
                alarms.get(i).setTime("00:00");
                alarms.get(i).setName("");
                if(alarms.get(i).getAlarmId()== - 1) {
                    alarms.get(i).setAlarmId(i);
                }
            }
            holder.name.setText(alarms.get(position).getName());
            holder.time.setText(alarms.get(position).getTime());
            holder.deleteAlarmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alarms.get(position).getTriggerHour() != -1 && alarms.get(position).getTriggerMinute() != -1) {
                        alarms.get(position).cancelAlarm(view.getContext());
                    }
                    alarms.remove(alarms.get(position));
                    notifyDataSetChanged();
                    holder.settingsDialog.dismiss();
                }
            });

            holder.timeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!holder.smartBtn.isChecked()) {
                        alarms.get(position).setAlarm(view.getContext(), holder.timePicker.getCurrentHour(), holder.timePicker.getCurrentMinute());
                        holder.time.setText(holder.timePicker.getCurrentHour() + ":" + holder.timePicker.getCurrentMinute());
                        holder.settingsTime.setText(holder.timePicker.getCurrentHour() + ":" + holder.timePicker.getCurrentMinute());
                        holder.switchBtn.setChecked(true);
                    }
                    else {
                        alarms.get(position).setSmartAlarm(view.getContext(), holder.timePicker.getCurrentHour(), holder.timePicker.getCurrentMinute());
                        holder.time.setText(holder.timePicker.getCurrentHour() + ":" + holder.timePicker.getCurrentMinute());
                        holder.settingsTime.setText(holder.timePicker.getCurrentHour() + ":" + holder.timePicker.getCurrentMinute());
                        holder.switchBtn.setChecked(true);
                    }
                    holder.timeDialog.dismiss();
                }
            });

            holder.switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (alarms.get(position).getTriggerHour() != -1 && alarms.get(position).getTriggerMinute() != -1)
                    if (isChecked){
                        alarms.get(position).setAlarm(view.getContext(),alarms.get(position).getTriggerHour(),alarms.get(position).getTriggerMinute());
                    }
                    else {
                        alarms.get(position).cancelAlarm(view.getContext());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return alarms.size();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveToFile(ContentAdapter.alarms);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (loadFromFile()!=null) {
            ContentAdapter.alarms = loadFromFile();
            contentAdapter.notifyDataSetChanged();
        }
    }

    static void saveToFile(ArrayList<MyAlarmManager> alarms){
        try {
            FileOutputStream fos = new FileOutputStream(SAVED_LIST);
            ObjectOutputStream oos= new ObjectOutputStream(fos);
            oos.writeObject(alarms);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static ArrayList<MyAlarmManager> loadFromFile(){
        try {
            FileInputStream fis = new FileInputStream(SAVED_LIST);
            ObjectInputStream ois= new ObjectInputStream(fis);
            ArrayList<MyAlarmManager> alarms=(ArrayList<MyAlarmManager>)ois.readObject();
            ois.close();
            return alarms;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
