package com.android.bdyr.Adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.android.bdyr.Fragments.BdyList;
import com.android.bdyr.Fragments.UpcomingBdy;

@SuppressWarnings ("ALL")
public class viewPagerAdapter extends FragmentStatePagerAdapter {
    public viewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new UpcomingBdy();
            case 1:
                return new BdyList();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
