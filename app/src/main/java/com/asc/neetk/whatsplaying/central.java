package com.asc.neetk.whatsplaying;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;


public class central extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    Drawable d;
   drawerAdapter mAdapter;
    private CharSequence mTitle;
    private String[] mDrawerItmes= {"Your Profile","Your Preferences","Your History"};

    String TITLES[] = {"Your Profile","Preferences","History","Logout"};
    int ICONS[] = {R.drawable.userprofileico,R.drawable.preferencesicon,R.drawable.historyicon,R.drawable.logouticon};


    String NAME = ParseUser.getCurrentUser().getUsername();

    ParseFile user1 = ParseUser.getCurrentUser().getParseFile("profilePic");
    View header;
    ImageView headerpic;


    private static final String TAG = central.class.getSimpleName();

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_central);

        if (user1 == null){  d = getResources().getDrawable(R.drawable.profile);
        }

        else {

            byte[] bitmapdata = new byte[0];
            try {
                bitmapdata = user1.getData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            d = new BitmapDrawable(getResources(), bitmap1);

        }





        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setDivider(null);
        mDrawerList.setDividerHeight(0);
        LayoutInflater inflater = getLayoutInflater();
        header = inflater.inflate(R.layout.header, mDrawerList, false);
        headerpic = (ImageView) header.findViewById(R.id.drawer_pic);
        headerpic.setImageDrawable(d);


        TextView name =(TextView) header.findViewById(R.id.drawer_name);
        name.setText(NAME);
        mDrawerList.addHeaderView(header, null, false);




        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mAdapter=new drawerAdapter(getApplicationContext(),TITLES,ICONS);

        // Add items to the ListView
        mDrawerList.setAdapter(mAdapter);
        // Set the OnItemClickListener so something happens when a
        // user clicks on an item.
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());




        // Enable ActionBar app icon to behave as action to toggle the NavigationDrawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(
                this,
                mDrawerLayout,
               R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClosed(View view) {

                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
            }

            public void onDrawerOpened(View drawerView) {

                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Set the default content area to item 0
        // when the app opens for the first time
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R. id.root_frame, TabbedFragment.newInstance()).commit();
        }



        //

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        if (drawerOpen) {


        }


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void open1() {


        if (user1 == null){  d = getResources().getDrawable(R.drawable.profile);
        }

        else {

            byte[] bitmapdata = new byte[0];
            try {
                bitmapdata = user1.getData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            d = new BitmapDrawable(getResources(), bitmap1);

        }


        // OR
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.header, mDrawerList, false);
        ImageView headerpic = (ImageView) header.findViewById(R.id.drawer_pic);
        headerpic.setImageDrawable(d);




        mDrawerLayout.addView(header);
    }






    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private class DrawerItemClickListener implements OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            navigateTo(i);

        }
    }

    private void navigateTo(int position) {

        switch (position) {

            case 1:
                Intent intent = new Intent(this, profilePic.class);
                startActivity(intent);
                break;


            case 2:
                Intent intent1 = new Intent(this, Preferences.class);
                startActivity(intent1);
                break;


            case 3:
                Intent intent2 = new Intent(this,MySongs.class);
                startActivity(intent2);
                break;


            case 4:

                final ProgressDialog progressDialog = ProgressDialog.show(central.this, "", "Logging Out...");
                new Thread() {

                    public void run() {


                        try {

                            // Logout current user
                            ParseUser.logOut();
                            Intent intent3 = new Intent(central.this, LoginSignupActivity.class);
                            startActivity(intent3);
                            finish();
                        } catch (Exception e) {

                            Log.e("tag", e.getMessage());

                        }


                        progressDialog.dismiss();

                    }

                }.start();


                break;






        }

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    Toast mtoast;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            mtoast.cancel();


            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        mtoast= Toast.makeText(this, "Please press back again to exit.", Toast.LENGTH_SHORT);

        mtoast.show();

        mHandler.postDelayed(mRunnable, 2000);
    }
}

