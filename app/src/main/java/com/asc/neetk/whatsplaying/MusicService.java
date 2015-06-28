package com.asc.neetk.whatsplaying;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by utk994 on 03/06/15.
 */
public class MusicService extends Service {

private MediaPlayer player;

//song list
public String url = "";

    boolean present= false;

//current position
private int songPosn;
    private final IBinder musicBind = new MusicBinder();



    public String songTitle="";

        @Override
        public void onCreate(){
            //create the service
            super.onCreate();

            songPosn=0;

            player = new MediaPlayer();

            initMusicPlayer();
        }





        public void initMusicPlayer(){
            player.setWakeMode(getApplicationContext(),
                    PowerManager.PARTIAL_WAKE_LOCK);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onPrepared(MediaPlayer fFmpegMediaPlayer) {
                    present = true;
                    fFmpegMediaPlayer.start();

                    Intent notIntent = new Intent(getApplicationContext(), tutsPlayer.class);
                    notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendInt = PendingIntent.getActivity(getApplicationContext(), 0,
                            notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification.Builder builder = new Notification.Builder(getApplicationContext());

                    builder.setContentIntent(pendInt)
                            .setSmallIcon(R.drawable.speakers7)
                            .setTicker(songTitle)
                            .setOngoing(true)
                            .setContentTitle("Playing")
                            .setContentText(songTitle);
                    Notification not = builder.build();

                    startForeground(NOTIFY_ID, not);

                }
            });
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer fFmpegMediaPlayer) {

                    fFmpegMediaPlayer.pause();

                }
            });
            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer fFmpegMediaPlayer, int i, int i1) {
                    fFmpegMediaPlayer.reset();
                    return false;
                }
            });
        }


    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }


    public void setUrl(String theUrl){
        url=theUrl;
    }
    public void setTitle(String theTitle){
        songTitle = theTitle;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }






    private static final int NOTIFY_ID=1;


    @Override
    public void onDestroy() {


        stopForeground(true);
    }



    public boolean present ()
    {
        return (present);

    }




    public void playSong(){
        player.reset();
        try{
            player.setDataSource(url);
        } catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        player.prepareAsync();

    }


    public int getPosn(){
        return player.getCurrentPosition();
    }

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPng(){
        return player.isPlaying();
    }

    public void pausePlayer(){
        player.pause();
    }


    public void seek(int posn){
        player.seekTo(posn);
    }

    public void go(){
        player.start();
    }


}