package com.asc.neetk.whatsplaying;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.parse.ParseObject;
import com.parse.ParseUser;


public class fouract extends Activity {




    @Override
    public void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        final View content = findViewById(android.R.id.content).getRootView();

       

        this.setTitle("");

        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
       window.setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);





        WindowManager.LayoutParams params = getWindow().getAttributes();


        params.gravity= Gravity.BOTTOM;



        this.getWindow().setAttributes(params);



        setContentView(R.layout.activity_fouract);
        Bundle extras = getIntent().getExtras();

            final String track1 = extras.getString("Track");
            final String artist1 = extras.getString("Artist");
                final String album1 = extras.getString("Album");
                final ParseUser currentUser = ParseUser.getCurrentUser();
                final String user=currentUser.getUsername();

                Button buttony = (Button) findViewById(R.id.yes);
                buttony.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ParseObject testObject = new ParseObject("Songs");
                        testObject.put("Track", track1);
                testObject.put("Artist", artist1);
                testObject.put("Album", album1);
                        testObject.put("User",currentUser);
                testObject.put("Username",user);
                testObject.saveInBackground();



                    fouract.this.finish();


                        ContentValues values = new ContentValues();
                        values.put(SongsDb.KEY_SONGNAME, track1);
                        values.put(SongsDb.KEY_ARTIST, artist1);
                        values.put(SongsDb.KEY_ALBUM,album1);

                        getContentResolver().insert(MyContentProvider.CONTENT_URI, values);









            }
        });


        Button buttonn = (Button) findViewById(R.id.no);
        buttonn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 fouract.this.finish();

            }
        });



        int finishTime = 3;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                fouract.this.finish();
            }
        }, finishTime * 1000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            return true;
        }
        return super.onTouchEvent(event);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


      //  getMenuInflater().inflate(R.menu.menu_fouract, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

  /*  //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);*/
        return true;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();


    }


    }



