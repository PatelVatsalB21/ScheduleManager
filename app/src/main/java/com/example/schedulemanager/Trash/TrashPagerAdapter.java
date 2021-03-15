package com.example.schedulemanager.Trash;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrashPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> TrashFragmentList = new ArrayList<>();

    public TrashPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void addFragment(Fragment fragment){
        TrashFragmentList.add(fragment);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return TrashFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return TrashFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
            title = "Tasks";
        else if (position == 1)
            title = "Notes";
        else if (position == 2)
            title = "Emails";
        return title;
    }
}
