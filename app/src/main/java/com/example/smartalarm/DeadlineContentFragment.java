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
import java.util.ArrayList;

public class DeadlineContentFragment extends Fragment {

    static DeadlineContentFragment.ContentAdapter contentAdapter;

    public static final String SAVED_DEADLINE_LIST="deadlineList.txt";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.item_deadline,null);
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

        Dialog settingsDialog;


        public ViewHolder(View view) {
            super(view);
            final Context context = view.getContext();

            settingsDialog = new Dialog(context);
            settingsDialog.setContentView(R.layout.deadlines_settings_dialog);

            timeLeft = (TextView) itemView.findViewById(R.id.timeLeftTextView);
            date = (TextView) itemView.findViewById(R.id.dateTextView);
            name = (TextView) itemView.findViewById(R.id.taskName);
            settingsDate = (TextView) settingsDialog.findViewById(R.id.date);
            settingsTime = (TextView) settingsDialog.findViewById(R.id.time);

            settingsName = (EditText) settingsDialog.findViewById(R.id.editName);

            settings = (Button) itemView.findViewById(R.id.settingsBtn);
            delete = (Button) settingsDialog.findViewById(R.id.deadlinesDeleteBtn);
            apply = (Button) settingsDialog.findViewById(R.id.deadlinesApplyBtn);




        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<DeadlineContentFragment.ViewHolder>{
        View view;
        static ArrayList<MyDeadlineManager> deadlines = new ArrayList<>();


        ContentAdapter(){
            deadlines.add(new MyDeadlineManager());
        }
        @Override
        public DeadlineContentFragment.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deadline,parent,false);
            return new DeadlineContentFragment.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final DeadlineContentFragment.ViewHolder holder, final int position) {




            holder.settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.settingsDialog.show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return deadlines.size();
        }
    }

}


