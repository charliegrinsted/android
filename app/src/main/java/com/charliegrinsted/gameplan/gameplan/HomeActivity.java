package com.charliegrinsted.gameplan.gameplan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.charliegrinsted.gameplan.R;
import com.charliegrinsted.gameplan.tabs.SlidingTabLayout;

import java.util.ArrayList;

public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // set the main layout for this activity

        ViewPager layoutPager = (ViewPager) findViewById(R.id.tabpager);
        SlidingTabLayout layoutTabs = (SlidingTabLayout) findViewById(R.id.slidingtabs);
        layoutTabs.setDistributeEvenly(true);
        // assign the viewPager and slidingTabLayout from activity_home

        ArrayList<Fragment> fragments = new ArrayList<>(3);
        fragments.add(new MyEventsFragment());
        fragments.add(new FindEventsFragment());
        fragments.add(new SettingsFragment());
        // add each tab into the arrayList

        SlidingTabManager slidingTabsAdapter = new SlidingTabManager(getSupportFragmentManager(), fragments);
        layoutPager.setAdapter(slidingTabsAdapter); // connect the adapter to the viewPager
        layoutTabs.setViewPager(layoutPager); // make the tabs relate to the viewPager

    }


}
