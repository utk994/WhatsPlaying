package com.asc.neetk.whatsplaying;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.MediaController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.voidplus.soundcloud.SoundCloud;
import de.voidplus.soundcloud.Track;


public class playSong extends Activity implements MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl{
    private static final String TAG = "AudioPlayer";

    public static final String AUDIO_FILE_NAME = "audioFileName";

    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private String url;

    private Handler handler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);









        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);

        mediaController = new MediaController(this);


        Bundle extras = getIntent().getExtras();
        String song = extras.getString("Songname");
        //String artist = extras.getString("Artist");


        try {
            url = new RetreiveUrl().execute(song).get();
            if (url != null) {
                Log.d("DIditWork", url);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "Could not open file " +url+ " for playback.", e);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaController.hide();
        mediaPlayer.stop();
        mediaPlayer.release();
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
    }



    public class RetreiveUrl extends AsyncTask<String, Void, String> {
        String use;
        ArrayList<Track> result;


        @Override
        protected String doInBackground(String... urls) {

            String song = urls[0];

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
            Log.d("abcs",use);
            return use;

        }

        @Override
        protected void onPostExecute(String url) {


        }
    }
}
