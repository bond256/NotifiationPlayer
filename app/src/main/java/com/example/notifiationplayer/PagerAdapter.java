package com.example.notifiationplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {
    private int countTab;
    private ArrayList<Fragment> fragmentList=new ArrayList<>();
    private ArrayList<String> titleList=new ArrayList<>();
    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        countTab=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PlayFragment();
            case 1:
                return new PlayListFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Play";
            case 1: return "Play List";
            default: return  " ";
        }
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
