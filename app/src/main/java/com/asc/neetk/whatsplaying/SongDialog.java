package com.asc.neetk.whatsplaying;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by utk994 on 30/04/15.
 */
public class SongDialog extends  android.support.v4.app.DialogFragment {


    public SongDialog() {
        // Empty constructor required for DialogFragment
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);



        Bundle bundle = this.getArguments();





        final String track1 = bundle.getString("SongName");
        final String artist1 = bundle.getString("Artist");
        final String album1 = bundle.getString("Album");

        TextView songname = (TextView) view.findViewById(R.id.songname);
        TextView artist = (TextView) view.findViewById(R.id.artist);
        TextView album = (TextView) view.findViewById(R.id.album);

        songname.setText(track1);
        artist.setText(artist1);
        album.setText(album1);



        Button listen= (Button) view.findViewById(R.id.listenonline);

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String acttrck = track1.replace(" ","%20");
                String actart = artist1.replace(" ","%20");

                final String url = "https://soundcloud.com/search/sounds?q="+acttrck+"%20"+actart ;





                final Intent browserIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
                startActivity(browserIntent);


            }
        });



        return view;
    }

}
