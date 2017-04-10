package com.example.smartalarm;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.Format;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DeadlineContentFragment extends Fragment {

    static DeadlineContentFragment.ContentAdapter contentAdapter;

    public static final String SAVED_DEADLINE_LIST="deadlineList.txt";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView=(RecyclerView)inflater.inflate(R.layout.deadline_recycler_view,container,false);
        contentAdapter=new DeadlineContentFragment.ContentAdapter();
        recyclerView.setAdapter(contentAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView timeLeft;
        TextView date;
        TextView name;
        TextView settingsDate;
        TextView settingsTime;

        EditText settingsName;

        Button settings;
        Button delete;
        Button apply;
        Button dateApplyBtn;
        Button timeApplyBtn;

        Dialog settingsDialog;
        Dialog datePicker;
        Dialog timePicker;

        DatePicker dPicker;
        TimePicker tPicker;


        public ViewHolder(View view) {
            super(view);
            final Context context = view.getContext();

            settingsDialog = new Dialog(context);
            settingsDialog.setContentView(R.layout.deadlines_settings_dialog);

            datePicker = new Dialog(context);
            datePicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePicker.setContentView(R.layout.deadlines_date_picker);

            timePicker = new Dialog(context);
            timePicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
            timePicker.setContentView(R.layout.time_picker);

            dPicker = (DatePicker)datePicker.findViewById(R.id.datePicker);

            tPicker = (TimePicker)timePicker.findViewById(R.id.timePicker);
            tPicker.setIs24HourView(true);

            date = (TextView) itemView.findViewById(R.id.dateTextView);
            name = (TextView) itemView.findViewById(R.id.taskName);
            settingsDate = (TextView) settingsDialog.findViewById(R.id.date);
            settingsTime = (TextView) settingsDialog.findViewById(R.id.time);

            settingsName = (EditText) settingsDialog.findViewById(R.id.editName);

            settings = (Button) itemView.findViewById(R.id.settingsBtn);
            delete = (Button) settingsDialog.findViewById(R.id.deadlinesDeleteBtn);
            apply = (Button) settingsDialog.findViewById(R.id.deadlinesApplyBtn);
            dateApplyBtn = (Button) datePicker.findViewById(R.id.datePickerBtn);
            timeApplyBtn = (Button) timePicker.findViewById(R.id.applyTimeButton);
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<DeadlineContentFragment.ViewHolder>{
        View view;
        static ArrayList<MyDeadlineManager> deadlines = new ArrayList<>();

        @Override
        public DeadlineContentFragment.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deadline,parent,false);
            return new DeadlineContentFragment.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final DeadlineContentFragment.ViewHolder holder, final int position) {
            final Calendar calendar = Calendar.getInstance();

            holder.name.setText(deadlines.get(position).getName());
            holder.date.setText(deadlines.get(position).getDate());
            holder.settingsDate.setText(deadlines.get(position).getDate());
            holder.settingsTime.setText(deadlines.get(position).getTime());

            if (deadlines.get(position).getDeadlineId() == -1){
                deadlines.get(position).setDeadlineId(position);
            }

            holder.settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.settingsDialog.show();
                }
            });

            holder.settingsDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.datePicker.show();
                }
            });

            holder.settingsTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.timePicker.show();
                }
            });

            holder.dateApplyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Format formatter = new SimpleDateFormat("dd.MM.yyyy");
                    calendar.set(Calendar.YEAR, holder.dPicker.getYear());
                    calendar.set(Calendar.MONTH, holder.dPicker.getMonth());
                    calendar.set(Calendar.DAY_OF_MONTH, holder.dPicker.getDayOfMonth());
                    holder.date.setText(formatter.format(calendar.getTimeInMillis()));
                    holder.settingsDate.setText(formatter.format(calendar.getTimeInMillis()));
                    deadlines.get(position).setDate(formatter.format(calendar.getTimeInMillis()));
                    holder.datePicker.dismiss();
                }
            });

            holder.timeApplyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Format formatter = new SimpleDateFormat("HH:mm");
                    calendar.set(Calendar.HOUR_OF_DAY, holder.tPicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, holder.tPicker.getCurrentMinute());
                    calendar.set(Calendar.SECOND, 0);
                    holder.settingsTime.setText(formatter.format(calendar.getTimeInMillis()));
                    deadlines.get(position).setTime(formatter.format(calendar.getTimeInMillis()));
                    holder.timePicker.dismiss();
                }
            });

            holder.apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deadlines.get(position).setDeadline(view.getContext(),calendar.getTimeInMillis());
                    if (String.valueOf(holder.settingsName.getText()) != "") {
                        holder.name.setText(holder.settingsName.getText());
                        deadlines.get(position).setName(String.valueOf(holder.settingsName.getText()));
                    }
                    holder.settingsDialog.dismiss();
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deadlines.get(position).cancelDeadline(view.getContext());
                    deadlines.remove(position);
                    holder.settingsDialog.dismiss();
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public int getItemCount() {
            return deadlines.size();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveToFile(ContentAdapter.deadlines,getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (loadFromFile(getContext())!=null) {
            ContentAdapter.deadlines = loadFromFile(getContext());
            contentAdapter.notifyDataSetChanged();
        }
    }

    static void saveToFile(ArrayList<MyDeadlineManager> deadlines, Context context){
        try {
            Log.d("FILE_DIR", context.getFilesDir().toString());
            FileOutputStream fos = new FileOutputStream(context.getFilesDir().toString() + "/" + SAVED_DEADLINE_LIST);
            ObjectOutputStream oos= new ObjectOutputStream(fos);
            oos.writeObject(deadlines);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static ArrayList<MyDeadlineManager> loadFromFile(Context context){
        try {
            FileInputStream fis = new FileInputStream(context.getFilesDir().toString() + "/" + SAVED_DEADLINE_LIST);
            ObjectInputStream ois= new ObjectInputStream(fis);
            ArrayList<MyDeadlineManager> deadlines=(ArrayList<MyDeadlineManager>)ois.readObject();
            ois.close();
            return deadlines;
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


