package com.asc.neetk.whatsplaying;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;


public class MyService extends Service {


    private static final String TAG = "MyService";

    private static final String BCAST_CONFIGCHANGED = "android.intent.action.CONFIGURATION_CHANGED";
    public static final String MY_SERVICE = "com.parse.WhatsPlaying.MyService";

    Handler handler = new Handler();


    private static Context mContext;
    String song1 = "";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate()");


        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.htc.music.metachanged");
        iF.addAction("com.google.android.music.metachanged");
        iF.addAction("com.soundcloud.android.metachanged");
        iF.addAction("fm.last.android.metachanged");
        iF.addAction("com.rhapsody.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.spotify.music.metadatachanged");
        iF.addAction("com.miui.player.metachanged");

        iF.addAction("com.sonyericsson.music.metachanged");
        iF.addAction("com.sec.android.app.music.metachanged");
        iF.addAction("com.samsung.sec.android.MusicPlayer.metachanged");
        iF.addAction("com.musixmatch.android.lyrify.metachanged");
        iF.addAction("com.amazon.mp3.metachanged");

        //iF.addAction("com.rdio.android.ui.metachanged");
        //iF.addAction("org.videolan.vlc.music.metachanged");
        //iF.addAction("com.real.IMP.metachanged");
        //iF.addAction("com.andrew.apollo.metachanged");


        registerReceiver(mReceiver, iF);


    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {


            final String artist1 = intent.getStringExtra("artist");
            final String album1 = intent.getStringExtra("album");
            final String track1 = intent.getStringExtra("track");
            Log.v("tag", artist1 + ":" + album1 + ":" + track1);
            //Toast.makeText(MyService.this, track1 + " " + artist1 + " " + album1, Toast.LENGTH_SHORT).show();

            Log.v("tag1", song1);




                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);

                // do something - or do it not
                Intent i = new Intent(getBaseContext(), fouract.class);

                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("Track", track1);
                i.putExtra("Artist", artist1);
                i.putExtra("Album", album1);
                getApplication().startActivity(i);














/*
            Intent dialogIntent = new Intent(getBaseContext(), fouract.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(dialogIntent);
*/

        }

    };
    ;

    @Override
    public void onDestroy() {


        unregisterReceiver(mReceiver);

        Log.v("SERVICE", "Service killed");

        super.onDestroy();
    }


}
