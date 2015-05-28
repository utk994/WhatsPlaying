package com.asc.neetk.whatsplaying;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;


public class Welcome extends Fragment {

    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_welcome, container, false);






        return rootView;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        super.onCreate(savedInstanceState);
        // Get the view from singleitemview.xml

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser.get("genLikes") == null)
        {currentUser.add("genLikes", "");}


        if (currentUser.get("Follows") == null)
        {currentUser.add("Follows", "");}

        if (currentUser.get("artLikes") == null)
        {currentUser.add("artLikes","");}

        // Convert currentUser into String
        String struser = currentUser.getUsername().toString();

        // Locate TextView in welcome.xml
        TextView txtuser = (TextView) getActivity().findViewById(R.id.txtuser);

        // Set the currentUser String into TextView
        txtuser.setText("You are logged in as " + struser);

        // Locate Button in welcome.xml
        logout = (Button) getActivity().findViewById(R.id.logout);

        // Logout Button Click Listener
        logout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {


                 final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "Logging Out...");
                new Thread() {

                    public void run() {


                        try {

                            // Logout current user
                            ParseUser.logOut();
                            Intent intent = new Intent(getActivity(), LoginSignupActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        } catch (Exception e) {

                            Log.e("tag", e.getMessage());

                        }


                        progressDialog.dismiss();

                    }

                }.start();


                // Retrieve the text entered from the EditText

            }

        });


    }
}

