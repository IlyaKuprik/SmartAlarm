package com.example.smartalarm;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    private static final String ID_SAVER="idSaver";

    static ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(1);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final FloatingActionButton alarmFab = (FloatingActionButton)findViewById(R.id.alarmFab);
        final FloatingActionButton deadlineFab = (FloatingActionButton)findViewById(R.id.deadlineFab);

        alarmFab.hide();
        deadlineFab.hide();

        alarmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmContentFragment.ContentAdapter.alarms.add(new MyAlarmManager());
                AlarmContentFragment.contentAdapter.notifyDataSetChanged();
            }
        });

        deadlineFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeadlineContentFragment.ContentAdapter.deadlines.add(new MyDeadlineManager());
                DeadlineContentFragment.contentAdapter.notifyDataSetChanged();
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case  0 :
                        alarmFab.show();
                        deadlineFab.hide();
                        break;
                    case 2 :
                        alarmFab.hide();
                        deadlineFab.show();;
                        break;
                    case 1 :
                        alarmFab.hide();
                        deadlineFab.hide();
                        break;
                    default :
                        alarmFab.hide();
                        deadlineFab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.settings) {
            Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

