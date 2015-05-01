package com.asc.neetk.whatsplaying;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by utk994 on 28/04/15.
 */
public class Appsett extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        //   ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "eUKyBmwblluSF4HNtjUFV6MCinRqTiyYw9Src8nf", "EMSKmnUuaSgffdQmkC3NDFmvVzwGAQBmv5ahlTtI");





        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
       defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
