package com.example.smartalarm;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
        Button add;
        public MiniViewHolder(View itemView) {
            super(itemView);
            editNameOfTask= (EditText)itemView.findViewById(R.id.editText);
            delete = (Button)itemView.findViewById(R.id.button);
            add = (Button)itemView.findViewById(R.id.add);
        }
    }

    static class MiniContentAdapter extends RecyclerView.Adapter<MiniViewHolder> {
        private ArrayList<ScrollElement> scroll = new ArrayList<>();
        MiniViewHolder holder;
        View view;
        public final static int COLOR = 0x1fffcf;

        @Override
        public MiniViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_scroll_view, parent, false);
            holder = new MiniViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MiniViewHolder holder, final int position) {
            holder.editNameOfTask.requestFocus();
            if (scroll.get(position).getId() == -1) {
                holder.add.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.GONE);
                holder.editNameOfTask.setBackgroundColor(COLOR);
                holder.editNameOfTask.setCursorVisible(true);
                holder.editNameOfTask.setEnabled(true);
                scroll.get(position).setId(position);
            }
            else{
                holder.add.setVisibility(View.INVISIBLE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.editNameOfTask.setBackgroundColor(Color.TRANSPARENT);
                holder.editNameOfTask.setCursorVisible(false);
                holder.editNameOfTask.setEnabled(false);
            }
            holder.editNameOfTask.setText(scroll.get(position).getName());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scroll.remove(position);
                    notifyDataSetChanged();
                }
            });
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scroll.get(position).setName(String.valueOf(holder.editNameOfTask.getText()));
                    holder.editNameOfTask.setBackgroundColor(Color.TRANSPARENT);
                    holder.editNameOfTask.setCursorVisible(false);
                    holder.editNameOfTask.setEnabled(false);
                    holder.add.setVisibility(View.GONE);
                    holder.delete.setVisibility(View.VISIBLE);
                    holder.editNameOfTask.requestFocus();
                }
            });
           holder.editNameOfTask.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   holder.add.setVisibility(View.VISIBLE);
                   holder.delete.setVisibility(View.GONE);
                   holder.editNameOfTask.setBackgroundColor(COLOR);
                   holder.editNameOfTask.setCursorVisible(true);
                   holder.editNameOfTask.setEnabled(true);
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
        Button settingsDate;

        EditText settingsName;

        Button settings;
        Button delete;
        Button apply;
        Button dateApplyBtn;
        Button timeApplyBtn;
        Button addScrollElement;
        Button blue;
        Button pink;
        Button green;
        Button red;
        Button chooseColor;

        Dialog settingsDialog;
        Dialog datePicker;
        Dialog timePicker;
        Dialog colorPicker;

        DatePicker dPicker;
        TimePicker tPicker;

        RecyclerView recyclerView;

        MiniContentAdapter scrollAdapter;

        ArrayList<ScrollElement> scroll;

        LinearLayout rightLayout;
        LinearLayout leftLayout;

        ScrollView scrollView;



        public ViewHolder(View view) {
            super(view);
            final Context context = view.getContext();

            settingsDialog = new Dialog(context);
            settingsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            settingsDialog.setContentView(R.layout.deadlines_settings_dialog);

            datePicker = new Dialog(context);
            datePicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePicker.setContentView(R.layout.deadlines_date_picker);

            timePicker = new Dialog(context);
            timePicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
            timePicker.setContentView(R.layout.time_picker);

            colorPicker = new Dialog(context);
            colorPicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
            colorPicker.setContentView(R.layout.color_picker_dialog);

            dPicker = (DatePicker)datePicker.findViewById(R.id.datePicker);

            tPicker = (TimePicker)timePicker.findViewById(R.id.timePicker);
            tPicker.setIs24HourView(true);

            date = (TextView) itemView.findViewById(R.id.dateTextView);
            name = (TextView) itemView.findViewById(R.id.taskName);
            settingsDate = (Button) settingsDialog.findViewById(R.id.date);
            settingsName = (EditText) settingsDialog.findViewById(R.id.editName);

            settings = (Button) itemView.findViewById(R.id.settingsBtn);
            delete = (Button) settingsDialog.findViewById(R.id.deadlinesDeleteBtn);
            apply = (Button) settingsDialog.findViewById(R.id.deadlinesApplyBtn);
            dateApplyBtn = (Button) datePicker.findViewById(R.id.datePickerBtn);
            timeApplyBtn = (Button) timePicker.findViewById(R.id.applyTimeButton);
            addScrollElement = (Button) settingsDialog.findViewById(R.id.addScroll);
            blue = (Button)colorPicker.findViewById(R.id.blue);
            pink = (Button)colorPicker.findViewById(R.id.pink);
            green = (Button)colorPicker.findViewById(R.id.green);
            red = (Button)colorPicker.findViewById(R.id.red);
            chooseColor = (Button)settingsDialog.findViewById(R.id.chooseColor);

            rightLayout = (LinearLayout)itemView.findViewById(R.id.rightLayout);
            leftLayout = (LinearLayout)itemView.findViewById(R.id.leftLayout);

            recyclerView=(RecyclerView) settingsDialog.findViewById(R.id.taskList);
            scrollAdapter = new MiniContentAdapter();
            recyclerView.setAdapter(scrollAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));


            scroll = new ArrayList<>();

            scrollView = (ScrollView)settingsDialog.findViewById(R.id.scrollView);

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
            class OnViewGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener{
                private int MAX_LENGTH;
                private View view;

                public OnViewGlobalLayoutListener(View view,int length){
                    this.view = view;
                    MAX_LENGTH = length;
                }


                @Override
                public void onGlobalLayout() {
                    if (view.getHeight() > MAX_LENGTH)
                        view.getLayoutParams().height = MAX_LENGTH;
                }
            }
            holder.recyclerView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(holder.recyclerView,400));
            holder.scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new OnViewGlobalLayoutListener(holder.scrollView,200));
            final Calendar calendar = Calendar.getInstance();
            final Context context = view.getContext();
            if (deadlines.get(position).getScroll()!= null) {
                holder.scrollAdapter.setScroll(deadlines.get(position).getScroll());
            }
            holder.name.setText(deadlines.get(position).getName());
            holder.settingsName.setText(deadlines.get(position).getName());
            holder.date.setText(deadlines.get(position).getDate());

            if (holder.scrollAdapter.getScroll() != null){
                deadlines.get(position).setScroll(holder.scrollAdapter.getScroll());
            }

            if (deadlines.get(position).getDeadlineId() == -1){
                deadlines.get(position).setDeadlineId(position);
            }

            switch (deadlines.get(position).getDeadlineColor()){
                case 0 :
                    holder.leftLayout.setBackgroundResource(R.color.blue_primary);
                    holder.rightLayout.setBackgroundResource(R.color.blue);
                    break;
                case 1 :
                    holder.leftLayout.setBackgroundResource(R.color.pink_primary);
                    holder.rightLayout.setBackgroundResource(R.color.pink);
                    break;
                case 2 :
                    holder.leftLayout.setBackgroundResource(R.color.green_primary);
                    holder.rightLayout.setBackgroundResource(R.color.green);
                    break;
                case 3 :
                    holder.leftLayout.setBackgroundResource(R.color.red_primary);
                    holder.rightLayout.setBackgroundResource(R.color.red);
                    break;
            }

            holder.chooseColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.colorPicker.show();
                }
            });

            holder.blue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deadlines.get(position).setDeadlineColor(0);
                    holder.leftLayout.setBackgroundResource(R.color.blue_primary);
                    holder.rightLayout.setBackgroundResource(R.color.blue);
                    holder.colorPicker.dismiss();
                }
            });

            holder.pink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deadlines.get(position).setDeadlineColor(1);
                    holder.leftLayout.setBackgroundResource(R.color.pink_primary);
                    holder.rightLayout.setBackgroundResource(R.color.pink);
                    holder.colorPicker.dismiss();
                }
            });

            holder.green.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deadlines.get(position).setDeadlineColor(2);
                    holder.leftLayout.setBackgroundResource(R.color.green_primary);
                    holder.rightLayout.setBackgroundResource(R.color.green);
                    holder.colorPicker.dismiss();
                }
            });

            holder.red.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deadlines.get(position).setDeadlineColor(3);
                    holder.leftLayout.setBackgroundResource(R.color.red_primary);
                    holder.rightLayout.setBackgroundResource(R.color.red);
                    holder.colorPicker.dismiss();
                }
            });

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


            holder.dateApplyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Format formatter = new SimpleDateFormat("dd.MM.yyyy");
                    calendar.set(Calendar.YEAR, holder.dPicker.getYear());
                    calendar.set(Calendar.MONTH, holder.dPicker.getMonth());
                    calendar.set(Calendar.DAY_OF_MONTH, holder.dPicker.getDayOfMonth());
                    holder.date.setText(formatter.format(calendar.getTimeInMillis()));
                    deadlines.get(position).setDate(formatter.format(calendar.getTimeInMillis()));
                    holder.datePicker.dismiss();
                    holder.timePicker.show();
                }
            });

            holder.timeApplyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Format formatter = new SimpleDateFormat("HH:mm");
                    calendar.set(Calendar.HOUR_OF_DAY, holder.tPicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, holder.tPicker.getCurrentMinute());
                    calendar.set(Calendar.SECOND, 0);
                    deadlines.get(position).setTime(formatter.format(calendar.getTimeInMillis()));
                    holder.timePicker.dismiss();
                }
            });

            View.OnClickListener addScroll = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deadlines.get(position).setScroll(holder.scrollAdapter.getScroll());
                    holder.scrollAdapter.getScroll().add(new ScrollElement());
                    holder.scrollAdapter.notifyDataSetChanged();
                }
            };

            holder.addScrollElement.setOnClickListener(addScroll);

            holder.apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!deadlines.get(position).getDate().equals("дата")) {
                        if (deadlines.get(position).isWorking()){
                            deadlines.get(position).setWorking(false);
                            deadlines.get(position).cancelDeadline(context);
                        }
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



