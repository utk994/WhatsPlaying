package com.asc.neetk.whatsplaying;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.soundcloud.android.crop.Crop;
import com.wefika.flowlayout.FlowLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class profilePic extends ActionBarActivity {


    Bitmap bitmap;
    private FlowLayout mFlowLayout1, mFlowLayout2,mFlowLayout3;
    ArrayList<String> genLikeList, artLikeList,userfollows,usernamefollows;
    TextView nogen,noart,nouser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pic); LinearLayout lay= (LinearLayout)findViewById(R.id.lin4);

        YoYo.with(Techniques.FadeIn).duration(1000).playOn(lay);

        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        mFlowLayout1 = (FlowLayout) findViewById(R.id.flowgen);
        mFlowLayout2 = (FlowLayout) findViewById(R.id.flowartist);
        mFlowLayout3 = (FlowLayout) findViewById(R.id.flowuser);


        noart = (TextView) findViewById(R.id.noart);
        nouser = (TextView) findViewById(R.id.nouser);
        nogen = (TextView) findViewById(R.id.nogen);




        final ImageView profilePic = (ImageView) findViewById(R.id.pictureView);
        Drawable drawable = getResources().getDrawable(R.drawable.profile);

        TextView username = (TextView) findViewById(R.id.profile_user);

        final TextView email = (TextView) findViewById(R.id.profile_email);


        username.setText(ParseUser.getCurrentUser().getUsername());

        email.setText(ParseUser.getCurrentUser().getEmail());


        TextView changepass = (TextView) findViewById(R.id.change_password);
        final TextView changeemail = (TextView) findViewById(R.id.change_email);

        final EditText getBio = (EditText) findViewById(R.id.enterbio);
        final TextView dispBio = (TextView) findViewById(R.id.bioview);
        Button changeBio = (Button) findViewById(R.id.bioadd);

        String bio = (String) ParseUser.getCurrentUser().get("Bio");
        if (bio == null) dispBio.setText("Enter a bio to interact better with other users. ");

        else dispBio.setText(bio);
        YoYo.with(Techniques.FadeIn).duration(500).playOn(dispBio);



        changeBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String actbio = getBio.getText().toString();

                if (actbio.equals("")) {

                    Toast.makeText(getApplicationContext(),
                            "Enter some text",
                            Toast.LENGTH_SHORT).show();
                } else {
                    dispBio.setText(actbio);
                    YoYo.with(Techniques.Pulse).duration(500).playOn(dispBio);

                    getBio.setText("");
                    hideSoftKeyboard(com.asc.neetk.whatsplaying.profilePic.this);

                    ParseUser currentUser = ParseUser.getCurrentUser();
                    currentUser.put("Bio", actbio);
                    currentUser.saveInBackground();
                }


            }


        });


        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(com.asc.neetk.whatsplaying.profilePic.this);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        com.asc.neetk.whatsplaying.profilePic.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialog1);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String userpass = userInput.getText().toString();
                                        if (userpass.equals("")) {

                                            Toast.makeText(getApplicationContext(),
                                                    "Enter some text",
                                                    Toast.LENGTH_SHORT).show();


                                        } else {

                                            ParseUser currentUser = ParseUser.getCurrentUser();
                                            currentUser.setPassword(userpass);
                                            currentUser.saveInBackground();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }

        });

        changeemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(com.asc.neetk.whatsplaying.profilePic.this);
                View promptsView = li.inflate(R.layout.prompts1, null);

                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
                        com.asc.neetk.whatsplaying.profilePic.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder1.setView(promptsView);

                final EditText userInput1 = (EditText) promptsView
                        .findViewById(R.id.editTextDialog2);

                // set dialog message
                alertDialogBuilder1
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String useremail = userInput1.getText().toString();

                                        if (useremail.equals("")) {

                                            Toast.makeText(getApplicationContext(),
                                                    "Enter some text",
                                                    Toast.LENGTH_SHORT).show();


                                        } else {

                                            email.setText(useremail);
                                            YoYo.with(Techniques.FadeIn).duration(500).playOn(email);


                                            ParseUser currentUser = ParseUser.getCurrentUser();
                                            currentUser.setEmail(useremail);
                                            currentUser.saveInBackground();
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog1 = alertDialogBuilder1.create();

                // show it
                alertDialog1.show();

            }

        });


        ParseFile user1 = ParseUser.getCurrentUser().getParseFile("profilePic");

        if (user1 == null) profilePic.setImageDrawable(drawable);

        else {

            byte[] bitmapdata = new byte[0];
            try {
                bitmapdata = user1.getData();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

            Drawable d = new BitmapDrawable(getResources(), bitmap1);

            profilePic.setImageDrawable(d);
            YoYo.with(Techniques.FadeIn).duration(500).playOn(profilePic);

        }


        ImageButton changepic = (ImageButton) findViewById(R.id.chngePic);

        changepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view != null)
                    Crop.pickImage(com.asc.neetk.whatsplaying.profilePic.this);


            }
        });
        userfollows = (ArrayList<String>) ParseUser.getCurrentUser().get("Follows");
        usernamefollows = (ArrayList<String>) ParseUser.getCurrentUser().get("folo");
        genLikeList = (ArrayList<String>) ParseUser.getCurrentUser().get("genLikes");
        artLikeList = (ArrayList<String>) ParseUser.getCurrentUser().get("artLikes");


        if (userfollows != null){



            for (int i = 0; i < userfollows.size(); i++) {
                String deletebllank = userfollows.get(i);
                if (deletebllank.equals("")) {
                    userfollows.remove(i);
                    continue;
                }

                addImage(mFlowLayout3, deletebllank);

            }
            if( userfollows.size()==0) {nouser.setVisibility(View.VISIBLE);}

        }

        if (genLikeList != null) {

            for (int i = 0; i < genLikeList.size(); i++) {
                String myString = genLikeList.get(i);

                if ((myString.equals(""))) {
                    genLikeList.remove(i);
                    continue;
                }

                addItem(mFlowLayout1, myString);


            }
            if (genLikeList.isEmpty()) {
                nogen.setVisibility(View.VISIBLE);
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

                if(artLikeList.isEmpty()) {noart.setVisibility(View.VISIBLE);}
            }


            final TextView genTag = (TextView) findViewById(R.id.gentag);

            final TextView artTag = (TextView) findViewById(R.id.arttag);


            Button subgen = (Button) findViewById(R.id.subgen);
            Button subart = (Button) findViewById(R.id.subart);

            if (subgen != null)


            {
                subgen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String msg = genTag.getText().toString();
                        genTag.setText("");


                        hideSoftKeyboard(profilePic.this);
                        if (msg.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Enter some text",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            nogen.setVisibility(View.GONE);
                            addItem(mFlowLayout1, msg);
                            genLikeList.add(msg);

                            ParseUser user = ParseUser.getCurrentUser();
                            user.add("genLikes", msg);
                            user.saveInBackground();

                        }

                    }
                });
            }

            if (subart != null)

            {


                subart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String msg = artTag.getText().toString();
                        artTag.setText("");

                        hideSoftKeyboard(profilePic.this);
                        if (msg.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Enter some text",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            noart.setVisibility(View.GONE);

                            addItem(mFlowLayout2, msg);
                            artLikeList.add(msg);
                            ParseUser user = ParseUser.getCurrentUser();
                            user.add("artLikes", msg);
                            user.saveInBackground();
                        }


                    }
                });
            }
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


        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int index = mFlowLayout.indexOfChild(view);

                Log.d("index", String.valueOf(index));
                String text = newView.getText().toString();

                if (mFlowLayout == mFlowLayout1) {

                    YoYo.with(Techniques.Wobble).duration(500).playOn(newView);

                    genLikeList.remove(index);
                    if (genLikeList.isEmpty()) nogen.setVisibility(View.VISIBLE);
                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("genLikes", genLikeList);
                    user.saveInBackground();


                } else {
                    YoYo.with(Techniques.Wobble).duration(500).playOn(newView);


                    artLikeList.remove(index);
                    if (artLikeList.isEmpty()) noart.setVisibility(View.VISIBLE);

                    ParseUser user = ParseUser.getCurrentUser();
                    user.put("artLikes", artLikeList);
                    user.saveInBackground();

                }


                mFlowLayout.removeViewAt(index);


            }
        });

    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    params.rightMargin = 10;
    params.bottomMargin = 10;
    newView.setLayoutParams(params);

    mFlowLayout.addView(newView);
    YoYo.with(Techniques.FadeIn).duration(500).playOn(newView);



}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            ImageView profilePic = (ImageView) findViewById(R.id.pictureView);
            profilePic.setImageURI(Crop.getOutput(result));
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));
                // Convert it to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.createScaledBitmap(bitmap, 200, 200, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);

                byte[] image = stream.toByteArray();

                // Create the ParseFile
                ParseFile file = new ParseFile("profile", image);
                // Upload the image into Parse Cloud
                file.saveInBackground();

                // Create a New Class called "ImageUpload" in Parse


                ParseUser currUser = ParseUser.getCurrentUser();


                currUser.put("profilePic", file);

                // Create the class and the columns
                currUser.saveInBackground();


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
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
                        YoYo.with(Techniques.Pulse).duration(500).playOn(newView);



                    } else {
                        newView.setImageDrawable(getResources().getDrawable(R.drawable.profile1));
                        mFlowLayout.addView(newView);
                        YoYo.with(Techniques.Pulse).duration(500).playOn(newView);


                    }

                    newView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            int index = mFlowLayout.indexOfChild(view);


                            userfollows.remove(index);
                            if (userfollows.isEmpty()) nouser.setVisibility(View.VISIBLE);
                            YoYo.with(Techniques.Wobble).duration(500).playOn(newView);
                            usernamefollows.remove(username);

                            ParseUser user = ParseUser.getCurrentUser();
                            user.put("Follows", userfollows );
                            user.put("folo", usernamefollows);

                            user.saveInBackground();

                            mFlowLayout.removeViewAt(index);

                            Toast.makeText(getApplicationContext(),"You unfollowed "+username,Toast.LENGTH_SHORT).show();
                        }


                    });


                }
            }
        });



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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}