package com.asc.neetk.whatsplaying.adapter;

/**
 * Created by utk994 on 05/04/15.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.asc.neetk.whatsplaying.Explore;
import com.asc.neetk.whatsplaying.Listen;
import com.asc.neetk.whatsplaying.Welcome;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new Explore();
            case 1:
                // Games fragment activity
                return new Welcome();
            case 2:
                // Movies fragment activity
                return new Listen();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }

}
