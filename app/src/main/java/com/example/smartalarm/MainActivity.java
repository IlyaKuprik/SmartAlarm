package com.example.smartalarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private static final String ID_SAVER="idSaver";
    private static String ID = "";
    int id = 0;

    private SharedPreferences idSaver;

    private SharedPreferences.Editor editor=idSaver.edit();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton alarmFab=(FloatingActionButton)findViewById(R.id.alarm_fab);

        idSaver=getSharedPreferences(ID_SAVER, Context.MODE_PRIVATE);

        alarmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idSaver.contains(ID)){
                    id = Integer.parseInt(idSaver.getString(ID,""));
                }
                MyAlarmManager alarm =new MyAlarmManager();
                alarm.setAlarmId(id);
                AlarmContentFragment.ContentAdapter.alarms.add(alarm);
                AlarmContentFragment.contentAdapter.notifyDataSetChanged();
                id++;
                editor.putString(ID,String.valueOf(id));
                editor.apply();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new AlarmContentFragment(), "Будильники");
        adapter.addFragment(new MainContentFragment(), "Главная");
        adapter.addFragment(new DeadlineContentFragment(), "Дедлайны");
        viewPager.setAdapter(adapter);
    }


    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

