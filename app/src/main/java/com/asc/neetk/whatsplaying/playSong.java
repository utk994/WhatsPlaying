package com.asc.neetk.whatsplaying;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;

import java.io.IOException;
import java.util.ArrayList;

import de.voidplus.soundcloud.SoundCloud;
import de.voidplus.soundcloud.Track;
import mehdi.sakout.dynamicbox.DynamicBox;


public class playSong extends ActionBarActivity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl{
    private static final String TAG = "AudioPlayer";

    public static final String AUDIO_FILE_NAME = "audioFileName";

    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    boolean playerthere =true;
    private String url;
    String song;
    String artist;
    DynamicBox box;

    private Handler handler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);

        mediaController = new MediaController(this);


        Bundle extras = getIntent().getExtras();
         song = extras.getString("Songname");
        artist= extras.getString("Artist");
        //String artist = extras.getString("Artist");
        box = new DynamicBox(this, findViewById(R.id.main_audio_view));
        View customView = getLayoutInflater().inflate(R.layout.songnotfound, null, false);
        box.addCustomView(customView, "noSong");
        box.setLoadingMessage("Loding ...");
        box.showLoadingLayout();




    }

    @Override
    protected void onStart() {

            new RetreiveUrl().execute(song);






        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();


        if ( playerthere)
        {
        mediaPlayer.release();
             mediaController.hide();}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //the MediaController will hide after 3 seconds - tap the screen to make it appear again
        mediaController.show();
        return false;
    }

    //--MediaPlayerControl methods----------------------------------------------------
    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
       return mediaPlayer.getAudioSessionId();
    }
    //--------------------------------------------------------------------------------

    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared");
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(findViewById(R.id.main_audio_view));

        handler.post(new Runnable() {
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });

        box.hideAll();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("onBackPressed() called");
        if (mediaPlayer!=null)mediaPlayer.release();

    }



    public class RetreiveUrl extends AsyncTask<String, Void, String> {
        String use;
        ArrayList<Track> result;


        @Override
        protected String doInBackground(String... urls) {




            try {
                SoundCloud soundcloud = new SoundCloud(
                        "1e650a9f949d3e717927abe90a7680f4",
                        "1d87b4e3d9930f116fb604b7f7e3b209"
                );

                result = soundcloud.findTrack(song);
                int i;

                for (i=0;i<result.size() && use == null;i++)
                use = result.get(i).getStreamUrl();


            } catch (Exception e) {
                e.printStackTrace();
            }

            return use;

        }



        @Override
        protected void onPostExecute(String url1) {
            try {

                if (url1 !=null)
                {mediaPlayer.setDataSource(url1);

                mediaPlayer.prepare();

                mediaPlayer.start();}


                else {box.showCustomView("noSong");
                    Button search = (Button) findViewById(R.id.searcho);

                    playerthere =false;

                    search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                             String acttrck = song.replace(" ", "%20");
                String actart = artist.replace(" ", "%20");

                final String url = "https://soundcloud.com/search/sounds?q=" + acttrck + "%20" + actart;



                final Intent browserIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
                startActivity(browserIntent);

                        }
                    });



                }
            } catch (IOException e) {
                Log.e(TAG, "Could not open file " +url1+ " for playback.", e);
            }



        }
    }
}
