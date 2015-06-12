package com.asc.neetk.whatsplaying;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

/**
 * Created by utk994 on 31/05/15.
 */
public class TabbedFragment extends Fragment {



    public static final String TAG = TabbedFragment.class.getSimpleName();
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;


    public static TabbedFragment newInstance() {
        return new TabbedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.centrallayout, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getChildFragmentManager());

        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);

       // mViewPager.setPageTransformer(true, new FlipHorizontalTransformer());


        return v;
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
             Bundle args = new Bundle();
            args.putInt(TabbedContentFragment.ARG_SECTION_NUMBER, position + 1);


            Fragment fragment= new TabbedContentFragment();
            switch(position) {
                case 0:
                    fragment = new ExploreRoot();

                    break;
                case 1:
                    fragment = new RootFragment();

                    break;

            }


            fragment.setArguments(args);
            return fragment;




        }

        @Override
        public int getCount() {

            return 2;
        }
    /*@Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }*/


        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Explore".toUpperCase(l);

                case 1:
                    return "Listen".toUpperCase(l);

            }
            return null;
        }




    }

    public static class TabbedContentFragment extends Fragment {


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public TabbedContentFragment() {




        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
            return rootView;
        }



    }


    }


