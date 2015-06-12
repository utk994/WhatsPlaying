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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.parse.LogInCallback;
import com.parse.ParseUser;


public class LoginSignupActivity extends Activity {
    // Declare Variables
    Button loginbutton;
    TextView signup;
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
        signup = (  TextView) findViewById(R.id.signuptxt);

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

                                                progressDialog.dismiss();
                                                // If user exist and authenticated, send user to Welcome.class
                                                Intent intent = new Intent(
                                                        LoginSignupActivity.this,
                                                        central.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);

                                                finish();
                                            } else {


                                                progressDialog.dismiss();

                                                YoYo.with(Techniques.Shake).duration(200).playOn(password);
                                                YoYo.with(Techniques.Shake).duration(200).playOn(username) ;

                                                Toast.makeText(
                                                        getApplicationContext(),
                                                        "Please check the details entered. ",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                        } catch (Exception e) {
                            progressDialog.dismiss();

                            Log.e("tag", e.getMessage());

                        }


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

                Intent intent = new Intent(
                        LoginSignupActivity.this,
                        signupActivity.class);
                startActivity(intent);

            }


                });

        TextView forgotPassButt = (TextView) findViewById(R.id.forgotpass);

        if (forgotPassButt != null) {

            forgotPassButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(
                            LoginSignupActivity.this,
                            forgotPass.class);
                    startActivity(intent);

                }
            });
        }


            }


        }







