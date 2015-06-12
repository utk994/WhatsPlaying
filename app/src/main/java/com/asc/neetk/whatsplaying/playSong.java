package com.asc.neetk.whatsplaying;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.triggertrap.seekarc.SeekArc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;
import de.voidplus.soundcloud.SoundCloud;
import de.voidplus.soundcloud.Track;
import mehdi.sakout.dynamicbox.DynamicBox;


public class playSong extends ActionBarActivity implements MediaPlayer.OnPreparedListener {
    private static final String TAG = "AudioPlayer";


    private MediaPlayer mediaPlayer;

    boolean playerthere = true;
    private String url;
    private Handler mHandler = new Handler();
    public  boolean bool = false;
    SeekArc bar;
    CircleImageView albumart;
    ImageView play;
    ImageView pause;
    ImageView replay;


    TextView albumname;
    TextView songname;
    TextView artistname;

    TextView time;

    Button hiddenBut;

    private Utilities utils;


    String song;


    String artist;
    String album;
    DynamicBox box;

    Toast t;

    Bitmap bitmap;
    int position;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circbartest);



        mediaPlayer = new MediaPlayer();


        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);


        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);


        utils = new Utilities();
        mediaPlayer.setOnPreparedListener(this);
        new RetreiveUrl().execute(song);




        Bundle extras = getIntent().getExtras();
        song = extras.getString("Songname");
        artist = extras.getString("Artist");
        album = extras.getString("Album");

        try {
            bitmap = BitmapFactory.decodeStream(getApplicationContext()
                    .openFileInput("myImage" + song));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        //String artist = extras.getString("Artist");
        box = new DynamicBox(this, findViewById(R.id.relativ1));
        View customView = getLayoutInflater().inflate(R.layout.songnotfound, null, false);
        box.addCustomView(customView, "noSong");
        box.setLoadingMessage("Loding ...");
        box.showLoadingLayout();


    }

    @Override
    public void onAttachedToWindow() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
    }



    @Override
    protected void onStart() {


        Log.d("ONStartexec", "EXEC");


        super.onStart();
    }


    @Override
    protected void onResume()
    {
        super.onResume();



    }





    @Override
    protected void onStop() {
        super.onStop();

        mHandler.removeCallbacks(mUpdateTimeTask);

        if (playerthere) {
            mediaPlayer.release();

        }
    }


    //--MediaPlayerControl methods----------------------------------------------------
    public void start() {
        play.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(300).playOn(play);
        YoYo.with(Techniques.FadeOut).duration(600).playOn(play);

        mediaPlayer.start();
    }

    public void pause() {
        pause.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(300).playOn(pause);
        YoYo.with(Techniques.FadeOut).duration(600).playOn(pause);
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
    public void onPrepared(final MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared");

        songname = (TextView) findViewById(R.id.player_song);
        artistname = (TextView) findViewById(R.id.player_artist);
        albumname = (TextView) findViewById(R.id.player_album);

        songname.setText(song);
        artistname.setText(artist);
        albumname.setText(album);


        bar = (SeekArc) findViewById(R.id.seekArc);
        pause = (ImageView) findViewById(R.id.pauseicon);
        play = (ImageView) findViewById(R.id.playicon);
        play.setVisibility(View.INVISIBLE);
        pause.setVisibility(View.INVISIBLE);
        bar.setVisibility(View.VISIBLE);
        bar.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekArc seekBar) {
                // remove message Handler from updating progress bar
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekArc seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);


                // forward or backward to certain seconds
                mediaPlayer.seekTo(seekBar.getProgress());

                // update timer progress again
                updateProgressBar();
            }
        });

        time = (TextView) findViewById(R.id.seektime);
        albumart = (CircleImageView) findViewById(R.id.circlealbum);

        replay = (ImageView) findViewById(R.id.replay);

        hiddenBut = (Button) findViewById(R.id.hiddenbutton);

        if (bitmap != null)
            albumart.setImageBitmap(bitmap);

        else {
            albumart.setImageDrawable(getResources().getDrawable(R.drawable.albumart));
        }
        albumart.setVisibility(View.VISIBLE);
        bar.setMax(mediaPlayer.getDuration());
        bar.setProgress(0);

        box.hideAll();

        hiddenBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {

                    pause();

                } else {


                    start();
                }
            }
        });


        start();

        updateProgressBar();


    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mediaPlayer.getDuration();
            long currentDuration = mediaPlayer.getCurrentPosition();

            time.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            //Log.d("Progress", ""+progress);
            bar.setProgress(mediaPlayer.getCurrentPosition());


            replay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    YoYo.with(Techniques.FadeOut).duration(500).playOn(replay);
                    replay.setVisibility(View.INVISIBLE);

                    mediaPlayer.seekTo(0);

                    start();


                }
            });


            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("onBackPressed() called");
        mHandler.removeCallbacks(mUpdateTimeTask);

        if (mediaPlayer != null) mediaPlayer.release();

    }


    public class RetreiveUrl extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {


            String use = null;

            try {
                SoundCloud soundcloud = new SoundCloud(
                        "1e650a9f949d3e717927abe90a7680f4",
                        "1d87b4e3d9930f116fb604b7f7e3b209"
                );

                ArrayList<Track> streamable_tracks = soundcloud.get("tracks", new String[]{

                        "q", song + " " + artist,
                        "filter", "downloadable",

                });

                Collections.sort(streamable_tracks, new Comparator<Track>() {
                    public int compare(Track o1, Track o2) {
                        if (o1.getPlaybackCount() == o2.getPlaybackCount())
                            return 0;
                        return o1.getPlaybackCount() < o2.getPlaybackCount() ? -1 : 1;
                    }
                });

                Collections.reverse(streamable_tracks);


                for (int j = 0; j < streamable_tracks.size(); j++)

                {


                    String title = streamable_tracks.get(j).getTitle().toLowerCase();

                    Boolean bool = title.toLowerCase().contains("remix") && !(song.toLowerCase().contains("remix"));
                    Boolean bool1 = title.toLowerCase().contains("live") && !(song.toLowerCase().contains("live"));
                    Boolean bool2 = title.toLowerCase().contains("soundtrack") && !(song.toLowerCase().contains("soundtrack"));
                    Boolean bool3 = title.toLowerCase().contains("cover") && !(song.toLowerCase().contains("cover"));


                    if (!bool && !bool1 && !bool2 && !bool3) {
                        use = streamable_tracks.get(j).getStreamUrl();
                        break;
                    }

                }


                Log.d("abcd", use);


            } catch (Exception e) {
                e.printStackTrace();
            }


            return use;

        }


        @Override
        protected void onPostExecute(String url1) {
            try {

                if (url1 != null) {

                    url = url1;


                    Log.d("check1", url1);
                    mediaPlayer.setDataSource(url1);
                    bool =true;

                    mediaPlayer.prepareAsync();


                } else {
                    box.showCustomView("noSong");
                    Button search = (Button) findViewById(R.id.searcho);

                    playerthere = false;

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
                Log.e(TAG, "Could not open file " + url1 + " for playback.", e);
            }


        }
    }
}
