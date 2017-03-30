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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DeadlineContentFragment extends Fragment {

    static AlarmContentFragment.ContentAdapter contentAdapter;

    public static final String SAVED_DEADLINE_LIST="deadlineList.txt";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_dedline,null);
        /* RecyclerView recyclerView=(RecyclerView)inflater.inflate(R.layout.recycler_view,container,false);
        contentAdapter=new AlarmContentFragment.ContentAdapter();
        recyclerView.setAdapter(contentAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView; */
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public ViewHolder(View view) {
            super(view);

        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<AlarmContentFragment.ViewHolder>{
        View view;
        @Override
        public AlarmContentFragment.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm,parent,false);
            return new AlarmContentFragment.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final AlarmContentFragment.ViewHolder holder, final int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

}


