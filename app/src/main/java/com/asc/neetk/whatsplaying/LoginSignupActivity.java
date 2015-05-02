package com.asc.neetk.whatsplaying;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class LoginSignupActivity extends Activity {
    // Declare Variables
    Button loginbutton;
    Button signup;
    String usernametxt;
    String passwordtxt;
    EditText password;
    EditText username;

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.activity_login);
        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        // Locate Buttons in main.xml
        loginbutton = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);

        // Login Button Click Listener
        loginbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                final ProgressDialog progressDialog = ProgressDialog.show(LoginSignupActivity.this, "", "Logging In...");

                new Thread() {

                    public void run() {

                        try {

                            usernametxt = username.getText().toString();
                            passwordtxt = password.getText().toString();

                            // Send data to Parse.com for verification
                            ParseUser.logInInBackground(usernametxt, passwordtxt,
                                    new LogInCallback() {

                                        @Override
                                        public void done(ParseUser parseUser, com.parse.ParseException e) {

                                            if (parseUser != null) {
                                                // If user exist and authenticated, send user to Welcome.class
                                                Intent intent = new Intent(
                                                        LoginSignupActivity.this,
                                                        central.class);
                                                startActivity(intent);

                                                finish();
                                            } else {
                                                Toast.makeText(
                                                        getApplicationContext(),
                                                        "Please check the details entered or Signup by entering desired username and password ",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                        } catch (Exception e) {

                            Log.e("tag", e.getMessage());

                        }


                        progressDialog.dismiss();

                    }

                }.start();


                // Retrieve the text entered from the EditText

            }
        });
        // Sign up Button Click Listener


        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {

                     loginbutton.performClick();
                }
                return false;
            }
        });






        signup.setOnClickListener(new View.OnClickListener() {


            public void onClick(View arg0) {




                        usernametxt = username.getText().toString();
                        passwordtxt = password.getText().toString();


                        // Force user to fill up the form
                        if (usernametxt.equals("") || passwordtxt.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Please entered the desired username and password and click Signup",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            final ProgressDialog progressDialog1 = ProgressDialog.show(LoginSignupActivity.this, "", "Signing Up...");
                            new Thread() {

                                public void run() {

                                    try {

                                        // Save new user data into Parse.com Data Storage
                                        ParseUser user = new ParseUser();
                                        user.setUsername(usernametxt);
                                        user.setPassword(passwordtxt);
                                        user.signUpInBackground(new SignUpCallback() {

                                            @Override
                                            public void done(com.parse.ParseException e) {
                                                if (e == null) {
                                                    // Show a simple Toast message upon successful registration
                                                    Toast.makeText(getApplicationContext(),
                                                            "Successfully Signed up, please log in.",
                                                            Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Sign up Error", Toast.LENGTH_LONG)
                                                            .show();
                                                }

                                            }
                                        });


                                    } catch (Exception e) {

                                        Log.e("tag", e.getMessage());

                                    }

                                    progressDialog1.dismiss();

                                }

                            }.start();
                        }
                    }
                });
            }


        }







