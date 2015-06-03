package com.asc.neetk.whatsplaying;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.voidplus.soundcloud.SoundCloud;
import de.voidplus.soundcloud.Track;


public class tutsPlayer extends ActionBarActivity implements MediaController.MediaPlayerControl {

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private MusicController controller;

    private String url;
    private String title;
    String song;
    private boolean paused = false, playbackPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuts_player);

        Bundle extras = getIntent().getExtras();
        song= extras.getString("Songname");
        setController();





    }

    @Override
    protected void onStart() {
        try {
            url = new RetreiveUrl().execute(song).get();

            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        super.onStart();

    }


    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list
            if (url != null) {
                musicSrv.setUrl(url);
                musicSrv.setTitle(title);
                musicBound = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    private void setController() {
        controller = new MusicController(this);
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.song_list));
        controller.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        playbackPaused = true;
        musicSrv.pausePlayer();

    }

    @Override
    public int getDuration() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if (musicSrv != null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int i) {
        musicSrv.seek(i);

    }

    @Override
    public boolean isPlaying() {
        if (musicSrv != null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    public class MusicController extends MediaController {

        public MusicController(Context c) {
            super(c);
        }

        public void hide() {
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {
            setController();
            paused = false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }


    public void songPicked(View view) {
        musicSrv.setUrl(url);
        musicSrv.playSong();
        if (playbackPaused) {
            setController();
            playbackPaused = false;
        }
        controller.show(0);
    }


    public class RetreiveUrl extends AsyncTask<String, Void, String> {
        String use;
        ArrayList<Track> result;


        @Override
        protected String doInBackground(String... urls) {

            String song = urls[0].substring(0, 6);


            try {
                SoundCloud soundcloud = new SoundCloud(
                        "1e650a9f949d3e717927abe90a7680f4",
                        "1d87b4e3d9930f116fb604b7f7e3b209"
                );



                result = soundcloud.findTrack(song);
            int i;

            for (i = 0; i < result.size() && use == null; i++) {
                Track acttrack = result.get(i);
                title = acttrack.getTitle();

                use = acttrack.getStreamUrl();
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("abcs", use);


            return use;

        }

        @Override
        protected void onPostExecute(String url) {


        }
    }


}

