package com.asc.neetk.whatsplaying;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class userProfile extends ActionBarActivity {
    FlowLayout mFlowLayout1, mFlowLayout2, mFlowLayout3;
    ArrayList<String> genLikeList, artLikeList;
    String actbio;
    ParseUser user;
    ArrayList<String> userfollows;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        final String actuser = intent.getStringExtra("User");

        this.setTitle(actuser);
        final ImageView profilePic = (ImageView) findViewById(R.id.viewpropic);

        mFlowLayout1 = (FlowLayout) findViewById(R.id.flowviewgen);
        mFlowLayout2 = (FlowLayout) findViewById(R.id.flowviewart);
        mFlowLayout3 = (FlowLayout) findViewById(R.id.flowviewUSER);

        final Button follow = (Button) findViewById(R.id.follow);


        TextView viewuser = (TextView) findViewById(R.id.viewuname);
        ;
        final TextView viewbio = (TextView) findViewById(R.id.viewuserbio);
        viewbio.setVisibility(View.INVISIBLE);
        TextView filler1 = (TextView) findViewById(R.id.filler1);

        TextView filler2 = (TextView) findViewById(R.id.filler2);
        TextView filler3 = (TextView) findViewById(R.id.filler3);

        follow.setVisibility(View.INVISIBLE);

        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
        query1.whereEqualTo("username", actuser);
        query1.setLimit(1);


        query1.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {


                    user = objects.get(0);

                    final String userId = user.getObjectId();

                    actbio = user.getString("Bio");


                    genLikeList = (ArrayList<String>) user.get("genLikes");


                    userfollows = (ArrayList<String>) user.get("Follows");


                    final ParseUser currentUser = ParseUser.getCurrentUser();


                    final ArrayList<String> currentUserfollows = (ArrayList) currentUser.get("folo");


                    if (currentUser.getUsername().equals(actuser)) {
                        follow.setVisibility(View.INVISIBLE);

                    } else if (currentUserfollows.contains(actuser)) {
                        follow.setBackgroundResource(R.drawable.unfollow);
                        follow.setVisibility(View.VISIBLE);

                    } else {
                        follow.setBackgroundResource(R.drawable.follow);
                        follow.setVisibility(View.VISIBLE);
                    }


                    follow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if (userfollows != null)

                            {
                                if (currentUser.getUsername().equals(actuser)) {
                                    follow.setVisibility(View.INVISIBLE);

                                } else if (currentUserfollows.contains(actuser)) {
                                    Toast.makeText(getApplicationContext(), "You unfollowed " + actuser, Toast.LENGTH_SHORT).show();
                                    userfollows.remove(actuser);

                                    currentUser.put("Follows", userfollows);
                                    currentUser.saveInBackground();

                                } else {
                                    // Log.d("Follows", userfollows.toString());
                                    currentUser.addUnique("Follows", userId);
                                    currentUser.addUnique("folo",actuser);
                                    currentUser.saveInBackground();
                                    Toast.makeText(getApplicationContext(), "Following " + actuser + "!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });


                    //  Log.d("List", genLikeList.toString());
                    artLikeList = (ArrayList<String>) user.get("artLikes");


                    if (genLikeList != null) {

                        for (int i = 0; i < genLikeList.size(); i++) {
                            String myString = genLikeList.get(i);


                            if ((myString.equals(""))) {
                                genLikeList.remove(i);
                                continue;
                            }

                            addItem(mFlowLayout1, myString);


                        }
                    }

                    if (artLikeList != null) {
                        for (int i = 0; i < artLikeList.size(); i++) {
                            String myString = artLikeList.get(i);

                            if ((myString.equals(""))) {
                                artLikeList.remove(i);
                                continue;
                            }

                            addItem(mFlowLayout2, myString);


                        }

                        Log.d("Follows", userfollows.toString());


                        for (int i = 0; i < userfollows.size(); i++) {
                            String deletebllank = userfollows.get(i);
                            if (deletebllank.equals("")) {
                                userfollows.remove(i);
                                continue;
                            }
                            addImage(mFlowLayout3, deletebllank);

                        }
                    }


                    if (actbio == null) {
                        viewbio.setText(actuser + " has not entered a bio.");
                        viewbio.setVisibility(View.VISIBLE);
                    } else {
                        viewbio.setText(actbio);
                        viewbio.setVisibility(View.VISIBLE);
                    }


                    ParseFile user1 = user.getParseFile("profilePic");


                    if (user1 != null)

                    {

                        byte[] bitmapdata = new byte[0];
                        try {
                            bitmapdata = user1.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                        //Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmap1, 50, 50, true);
                        // bitmap1.recycle();

                        Drawable d = new BitmapDrawable(getResources(), bitmap1);
                        profilePic.setImageDrawable(d);


                    } else {
                        profilePic.setImageDrawable(getResources().getDrawable(R.drawable.editicon));
                    }
                    // Something went wrong.
                }
            }
        });


        filler1.setText("Music Genres that " + actuser + " listens to");

        filler2.setText("Artists that " + actuser + " listens to");

        filler3.setText("Users that " + actuser + " follows");


        viewuser.setText(actuser);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addItem(final FlowLayout mFlowLayout, String message) {


        final TextView newView = new TextView(this);
        newView.setGravity(Gravity.CENTER);
        TableRow.LayoutParams params1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        newView.setLayoutParams(params1);
        newView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        newView.setTextColor(getResources().getColor(R.color.white));
        newView.setBackgroundResource(R.drawable.roundedrect);
        newView.setPadding(10, 10, 10, 10);
        newView.setText(message);


        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.rightMargin = 10;
        params.bottomMargin = 10;
        newView.setLayoutParams(params);

        mFlowLayout.addView(newView);
    }


    public void addImage(final FlowLayout mFlowLayout, String user1) {


        final CircleImageView newView = new CircleImageView(this);


        newView.setImageDrawable(getResources().getDrawable(R.drawable.profile));

        newView.setPadding(2, 2, 2, 2);
        newView.setBorderWidth(2);

        FlowLayout.LayoutParams params1 = new FlowLayout.LayoutParams(80, 80);
        params1.rightMargin = 10;
        params1.bottomMargin = 10;
        newView.setLayoutParams(params1);

        ParseQuery<ParseUser> querytemp = ParseUser.getQuery();
        querytemp.whereEqualTo("objectId", user1);
        querytemp.setLimit(1);


        querytemp.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {


                    ParseUser usertemp = objects.get(0);
                    final String username = usertemp.getUsername();


                    final ParseFile user2 = usertemp.getParseFile("profilePic");


                    if (user2 != null)

                    {

                        byte[] bitmapdata = new byte[0];
                        try {
                            bitmapdata = user2.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);


                        Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmap1, 60, 60, true);
                        bitmap1.recycle();

                        Drawable d = new BitmapDrawable(getResources(), bitmapsimplesize);
                        newView.setImageDrawable(d);
                        mFlowLayout.addView(newView);


                    } else {
                        newView.setImageDrawable(getResources().getDrawable(R.drawable.editicon));
                        mFlowLayout.addView(newView);

                    }

                    newView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent i = new Intent(userProfile.this, userProfile.class);
                            i.putExtra("User", username);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            startActivity(i);
                            finish();


                        }
                    });


                }
            }
        });


    }

}
