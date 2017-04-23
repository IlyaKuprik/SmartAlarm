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
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

    static class MiniViewHolder extends RecyclerView.ViewHolder{
        EditText editNameOfTask;
        Button delete;
        CheckBox isDone;
        public MiniViewHolder(View itemView) {
            super(itemView);
            editNameOfTask= (EditText)itemView.findViewById(R.id.editText);
            delete = (Button)itemView.findViewById(R.id.button);
            isDone = (CheckBox)itemView.findViewById(R.id.checkBox);
        }
    }

    static class MiniContentAdapter extends RecyclerView.Adapter<MiniViewHolder> {
        private ArrayList<ScrollElement> scroll = new ArrayList<>(0);
        MiniViewHolder holder;
        View view;

        @Override
        public MiniViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_scroll_view, parent, false);
            holder = new MiniViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MiniViewHolder holder, final int position) {
            scroll.get(position).setName(String.valueOf(holder.editNameOfTask.getText()));
            scroll.get(position).setDone(holder.isDone.isChecked());
            holder.editNameOfTask.setText(scroll.get(position).getName());
            holder.isDone.setChecked(scroll.get(position).isDone());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scroll.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return scroll.size();
        }

        public ArrayList<ScrollElement> getScroll() {
            return scroll;
        }

        public void setScroll(ArrayList<ScrollElement> scroll) {
            this.scroll = scroll;
            notifyDataSetChanged();
        }

        public void clean(){
            int size = scroll.size();
            if (size > 0){
                for (int i = 0; i < size ; i++) {
                    scroll.remove(0);
                }
                notifyItemRangeRemoved(0,size);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
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
        Button addScrollElement;

        Dialog settingsDialog;
        Dialog datePicker;
        Dialog timePicker;

        DatePicker dPicker;
        TimePicker tPicker;

        RecyclerView recyclerView;

        MiniContentAdapter scrollAdapter;

        ArrayList<ScrollElement> scroll;

        private static class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener{
            private final static int MAX_LENGTH = 400;
            private View view;

            public OnViewGlobalLayoutListener(View view){
                this.view = view;
            }

            @Override
            public void onGlobalLayout() {
                if (view.getHeight() > MAX_LENGTH)
                    view.getLayoutParams().height = MAX_LENGTH;
            }
        }

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
            addScrollElement = (Button) settingsDialog.findViewById(R.id.addScroll);

            recyclerView=(RecyclerView) settingsDialog.findViewById(R.id.taskList);
            scrollAdapter = new MiniContentAdapter();
            recyclerView.setAdapter(scrollAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.getViewTreeObserver()
                   .addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(recyclerView));

            scroll = new ArrayList<>();
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
            final Context context = view.getContext();
            if (deadlines.get(position).getScroll()!= null) {
                holder.scrollAdapter.setScroll(deadlines.get(position).getScroll());
            }
            holder.name.setText(deadlines.get(position).getName());
            holder.date.setText(deadlines.get(position).getDate());
            holder.settingsDate.setText(deadlines.get(position).getDate());
            holder.settingsTime.setText(deadlines.get(position).getTime());

            if (holder.scrollAdapter.getScroll() != null){
                deadlines.get(position).setScroll(holder.scrollAdapter.getScroll());
            }

            if (deadlines.get(position).getDeadlineId() == -1){
                deadlines.get(position).setDeadlineId(position);
            }

            holder.settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.settingsDialog.show();
                }
            });

            holder.settingsName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.settingsName.selectAll();
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

            holder.addScrollElement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.scrollAdapter.getScroll().add(new ScrollElement());
                    deadlines.get(position).setScroll(holder.scrollAdapter.getScroll());
                    holder.scrollAdapter.notifyDataSetChanged();
                }
            });

            holder.apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!holder.settingsTime.getText().equals("время") || !holder.settingsDate.getText().equals("дата")) {
                        if (!holder.settingsName.getText().equals("")) {
                            holder.name.setText(holder.settingsName.getText());
                            deadlines.get(position).setName(String.valueOf(holder.settingsName.getText()));
                        }
                        deadlines.get(position).setDeadline(context, calendar.getTimeInMillis(), String.valueOf(holder.settingsName.getText()));
                        holder.settingsDialog.dismiss();
                        Toast.makeText(context, "Событие установлено", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(context, "Событие не установлено\n" +
                            "Пожалуйста,выберите дату и время", Toast.LENGTH_SHORT).show();
                    deadlines.get(position).setScroll(holder.scrollAdapter.getScroll());
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.scrollAdapter.clean();
                    deadlines.get(position).cancelDeadline(context);
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

    private static void saveToFile(ArrayList<MyDeadlineManager> deadlines, Context context){
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



