package com.example.music;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;


public class ViewPagerAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragmentList = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                Home home = Home.getInstance();
                fragmentList.add(home);
                return fragmentList.get(0);
            case 1:
                fragmentList.add(PlayList.getInstance());
                return fragmentList.get(1);
            case 2:
                fragmentList.add(Library.getInstance());
                return fragmentList.get(2);
            default:
                return fragmentList.get(0);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
