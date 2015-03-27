package com.charliegrinsted.gameplan.gameplan;

import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class SlidingTabManager extends FragmentPagerAdapter {

    private ArrayList<android.support.v4.app.Fragment> fragments;

    // manually define the tab numbers and titles for the fragments
    public static final int MYEVENTS = 0;
    public static final int FINDEVENTS = 1;
    public static final int MYPROFILE = 2;

    public static final String TAB_MYEVENTS = "MY EVENTS";
    public static final String TAB_FINDEVENTS = "FIND EVENTS";
    public static final String TAB_SETTINGS = "MY PROFILE";

    public SlidingTabManager(android.support.v4.app.FragmentManager fm, ArrayList<android.support.v4.app.Fragment> fragments){
        super(fm);
        this.fragments = fragments;
    }

    public android.support.v4.app.Fragment getItem(int pos){
        return fragments.get(pos);
    }

    public int getCount(){
        return fragments.size();
    }

    // return titles based on viewPager position
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case MYEVENTS:
                return TAB_MYEVENTS;
            case FINDEVENTS:
                return TAB_FINDEVENTS;
            case MYPROFILE:
                return TAB_SETTINGS;
            default:
                break;
        }
        return null;
    }
}