package com.asc.neetk.whatsplaying;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class signupActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final TextView username = (TextView) findViewById(R.id.username1);
        final TextView password = (TextView) findViewById(R.id.password1);
        final TextView passmatch = (TextView) findViewById(R.id.passconfirm);

        final TextView email = (TextView) findViewById(R.id.email);
        final Button signup = (Button) findViewById(R.id.signupbutton);

        passmatch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                ImageView passmatchicon = (ImageView) findViewById(R.id.passmatchicon);
                passmatchicon.setVisibility(View.INVISIBLE);



                String passtxt = password.getText().toString();
                 String passmatchtxt = passmatch.getText().toString();

                email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_GO) {

                            signup.performClick();
                        }
                        return false;
                    }
                });



                if (!(passtxt.equals(passmatchtxt)))

                {
                    passmatchicon.setVisibility(View.VISIBLE);

                } else {
                    passmatchicon.setVisibility(View.INVISIBLE);
                }


            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  final String passtxt = password.getText().toString();
                 String passmatchtxt = passmatch.getText().toString();
                  final String emailtxt = email.getText().toString();

                final String usertxt = username.getText().toString();



                if (usertxt.equals("") || passtxt.equals("") || emailtxt.equals("") || passmatchtxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please fill in all the fields",
                            Toast.LENGTH_SHORT).show();
                } else if (!(passtxt.equals(passmatchtxt))) {
                    Toast.makeText(getApplicationContext(),
                            "Passwords do not match",
                            Toast.LENGTH_SHORT).show();
                } else {



                    final ProgressDialog progressDialog1 = ProgressDialog.show(signupActivity.this, "", "Signing Up...");
                    new Thread() {

                        public void run() {


                            try {
                                ParseUser user = new ParseUser();
                                user.setUsername(usertxt);
                                user.setPassword(passtxt);
                                user.setEmail(emailtxt);


                                user.signUpInBackground(new SignUpCallback() {
                                    @Override
                                    public void done(com.parse.ParseException e) {
                                        if (e == null) {
                                            progressDialog1.dismiss();
                                            Toast.makeText(getApplicationContext(),
                                                    "Successfully Signed up, please log in.",
                                                    Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(
                                                    signupActivity.this,
                                                    LoginSignupActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);


                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Sign up Error", Toast.LENGTH_SHORT)
                                                    .show();
                                            progressDialog1.dismiss();

                                        }

                                    }

                                });
                            } catch (Exception e) {

                                Log.e("tag", e.getMessage());
                                progressDialog1.dismiss();

                            }


                        }


                    }.start();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
