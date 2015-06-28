package com.asc.neetk.whatsplaying;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.triggertrap.seekarc.SeekArc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;
import de.voidplus.soundcloud.SoundCloud;
import de.voidplus.soundcloud.Track;
import mehdi.sakout.dynamicbox.DynamicBox;


public class tutsPlayer extends Activity {

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private static final String TAG = "AudioPlayer";

    Handler mHandler = new Handler();


    private String url;


    private boolean paused = false, playbackPaused = false;

    private RetreiveUrl task;

    private RetrieveArt task2;


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




    Bitmap bitmap;
    String imageurl;

    ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circbartest);


        imageLoader = ImageLoader.getInstance();


        box = new DynamicBox(this, findViewById(R.id.relativm));

        utils = new Utilities();

        View customView = getLayoutInflater().inflate(R.layout.songnotfound, null, false);
        box.addCustomView(customView, "noSong");

        View customView1 = getLayoutInflater().inflate(R.layout.playerload, null, false);
        box.addCustomView(customView1, "loading");






        box.showCustomView("loading");

        Bundle extras = getIntent().getExtras();
        song = extras.getString("Songname");
        artist = extras.getString("Artist");
        album = extras.getString("Album");


            StringBuilder stringBuilder = new StringBuilder("http://ws.audioscrobbler.com/2.0/");
            stringBuilder.append("?method=album.getinfo");
            stringBuilder.append("&api_key=");
            stringBuilder.append("3d4c79881824afd6b4c7544b753d1024");


            try {
                stringBuilder.append("&artist=" + URLEncoder.encode(artist, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            try {
                stringBuilder.append("&album=" + URLEncoder.encode(album, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            task2= (RetrieveArt) new RetrieveArt().execute(stringBuilder.toString());



        task = (RetreiveUrl) new RetreiveUrl().execute(song, artist);


    }

    @Override
    protected void onStart() {


        super.onStart();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);



        if (musicSrv != null && musicSrv.present() && musicSrv.isPng()) {


            setContentView(R.layout.activity_circbartest);


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


            songname.setText(song);
            artistname.setText(artist);
            albumname.setText(album);


            time = (TextView) findViewById(R.id.seektime);


            time.setText("" + utils.milliSecondsToTimer(musicSrv.getPosn()));


            if (musicSrv != null) bar.setProgress(musicSrv.getPosn());

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
                    musicSrv.seek(seekBar.getProgress());

                    // update timer progress again
                    updateProgressBar();
                }
            });

            albumart = (CircleImageView) findViewById(R.id.circlealbum);


            replay = (ImageView) findViewById(R.id.replay);


            hiddenBut = (Button) findViewById(R.id.hiddenbutton);


            albumart.setVisibility(View.VISIBLE);


            if (imageurl != null)
                imageLoader.displayImage(imageurl, albumart);

            else {
                albumart.setImageDrawable(getResources().getDrawable(R.drawable.albumart));
            }


            hiddenBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (musicSrv.isPng()) {

                        pause();

                    } else {


                        start();
                    }
                }
            });
        }

    }


    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            //pass list

            musicSrv.setUrl(url);
            musicSrv.setTitle(song);

            musicBound = true;





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
                    musicSrv.seek(seekBar.getProgress());

                    // update timer progress again
                    updateProgressBar();
                }
            });

            time = (TextView) findViewById(R.id.seektime);
            albumart = (CircleImageView) findViewById(R.id.circlealbum);




            replay = (ImageView) findViewById(R.id.replay);



            hiddenBut = (Button) findViewById(R.id.hiddenbutton);


            albumart.setVisibility(View.VISIBLE);

            if (imageurl != null)
                imageLoader.displayImage(imageurl, albumart);


            else {
                albumart.setImageDrawable(getResources().getDrawable(R.drawable.albumart));
            }
            bar.setMax(musicSrv.getDur());
            bar.setProgress(0);

            musicSrv.playSong();

            hiddenBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (musicSrv.isPng()) {

                        pause();

                    } else {


                        start();
                    }
                }
            });

            updateProgressBar();


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void start() {
        play.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(300).playOn(play);
        YoYo.with(Techniques.FadeOut).duration(600).playOn(play);

        musicSrv.go();
    }

    private void pause() {
        pause.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FadeIn).duration(300).playOn(pause);
        YoYo.with(Techniques.FadeOut).duration(600).playOn(pause);
        musicSrv.pausePlayer();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    protected void onDestroy() {


        mHandler.removeCallbacks(null);

        task.cancel(true);
        if (task2 !=null)        task2.cancel(true);

        if (playIntent != null)

            unbindService(musicConnection);
        this.stopService(new Intent(this, MusicService.class));

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
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paused) {

            paused = false;
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
    }




    public void songPicked(View view) {
        musicSrv.setUrl(url);
        musicSrv.playSong();
        if (playbackPaused) {

            playbackPaused = false;

        }

    }


    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {





            if (musicSrv != null && musicSrv.present() && musicSrv.isPng()) {


                box.hideAll();



                long totalDuration = musicSrv.getDur();

                bar.setMax(musicSrv.getDur());
                long currentDuration = musicSrv.getPosn();

                time.setText("" + utils.milliSecondsToTimer(currentDuration));

                // Updating progress bar
                int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                bar.setProgress(musicSrv.getPosn());


            }


            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


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

            if (url1 != null) {

                url = url1;


                Log.d("check1", url1);


                if (playIntent == null) {
                    playIntent = new Intent(tutsPlayer.this, MusicService.class);
                    bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
                    startService(playIntent);


                }
            } else {

                box.showCustomView("noSong");
                Button search = (Button) findViewById(R.id.searcho);


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


        }
    }


    public class RetrieveArt extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            String albumArtUrl = null;
            try {
                XMLParser parser = new XMLParser();
                String xml = parser.getXmlFromUrl(urls[0]); // getting XML from URL
                Document doc = parser.getDomElement(xml);
                NodeList nl = doc.getElementsByTagName("image");
                for (int i = 0; i < nl.getLength(); i++) {
                    Element e = (Element) nl.item(i);

                    if (e.getAttribute("size").contentEquals("large")) {
                        albumArtUrl = parser.getElementValue(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return albumArtUrl;
        }


        protected void onPostExecute(final String url1) {


            if (url1 != null && !url1.equals("")) {

                imageurl=url1;


                       Log.d("there", url1);


                   }


               }
            }
}













