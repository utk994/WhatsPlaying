package com.asc.neetk.whatsplaying;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class profilePic extends ActionBarActivity {


    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pic);

        if (getActionBar() != null)
        getActionBar().setDisplayHomeAsUpEnabled(true);


        ImageView profilePic = (ImageView) findViewById(R.id.pictureView);
        Drawable drawable = getResources().getDrawable(R.drawable.profile);

        TextView username = (TextView) findViewById(R.id.profile_user);

        final TextView email = (TextView) findViewById(R.id.profile_email);




        username.setText(ParseUser.getCurrentUser().getUsername());

        email.setText(ParseUser.getCurrentUser().getEmail());


        TextView changepass = (TextView) findViewById(R.id.change_password);
        final TextView changeemail = (TextView) findViewById(R.id.change_email);


        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(com.asc.neetk.whatsplaying.profilePic.this );
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
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String userpass = userInput.getText().toString();
                                        ParseUser currentUser = ParseUser.getCurrentUser();
                                        currentUser.setPassword(userpass);
                                        currentUser.saveInBackground();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
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
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        String useremail = userInput1.getText().toString();

                                        email.setText(useremail);

                                        ParseUser currentUser = ParseUser.getCurrentUser();
                                        currentUser.setEmail(useremail);
                                        currentUser.saveInBackground();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
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

                        if (user1== null) profilePic.setImageDrawable(drawable);

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
                                            }










        ImageButton changepic = (ImageButton) findViewById(R.id.chngePic);

        changepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( view != null)
                Crop.pickImage(com.asc.neetk.whatsplaying.profilePic.this);






            }
        });

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
}
