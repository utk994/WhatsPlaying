package com.asc.neetk.whatsplaying;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by utk994 on 30/04/15.
 */


public class SongDialog extends DialogFragment {


    public SongDialog() {
        // Empty constructor required for DialogFragment
    }

    View mView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mView =view;




        Bundle bundle = this.getArguments();

        final String track1 = bundle.getString("SongName");
        final String artist1 = bundle.getString("Artist");
        final String album1 = bundle.getString("Album");
        final String user1 = bundle.getString("User");


        final TextView user = (TextView) view.findViewById(R.id.sharedby);
        final TextView songname = (TextView) view.findViewById(R.id.songname);
        final TextView artist = (TextView) view.findViewById(R.id.artist);
        final TextView album = (TextView) view.findViewById(R.id.album);


        img =(CircleImageView) mView.findViewById(R.id.coverfor);
        img.setVisibility(View.INVISIBLE);





        songname.setText(track1);
        artist.setText(artist1);
        album.setText(album1);
        user.setText(user1);


        Button listen = (Button) view.findViewById(R.id.listenonline);

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String acttrck = track1.replace(" ", "%20");
                String actart = artist1.replace(" ", "%20");

                final String url = "https://soundcloud.com/search/sounds?q=" + acttrck + "%20" + actart;


                final Intent browserIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
                startActivity(browserIntent);


            }
        });

        StringBuilder stringBuilder = new StringBuilder("http://ws.audioscrobbler.com/2.0/");
        stringBuilder.append("?method=album.getinfo");
        stringBuilder.append("&api_key=");
        stringBuilder.append("3d4c79881824afd6b4c7544b753d1024");

        try {
            stringBuilder.append("&artist=" + URLEncoder.encode(artist1, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {
            stringBuilder.append("&album=" + URLEncoder.encode(album1, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {
            String url = new RetrieveArt().execute(stringBuilder.toString()).get();

            Log.d("URL",url);

            new LoadImage().execute(url);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        ImageButton viewProfile = (ImageButton) view.findViewById(R.id.viewprofile);

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(getActivity(), userProfile.class);
                i.putExtra("User", user1);
                startActivity(i);
                // getActivity().overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
            }
        });


        return view;
    }

    CircleImageView img;

    Bitmap bitmap;

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){

                img =(CircleImageView) mView.findViewById(R.id.coverfor);
                img.setVisibility(View.VISIBLE);



                YoYo.with(Techniques.RotateIn)
                        .duration(500)
                        .playOn(img);



                img.setImageBitmap(image);

            }else{

                img =(CircleImageView) mView.findViewById(R.id.coverfor);

                img.setImageDrawable(getResources().getDrawable(R.drawable.albumart));
                img.setVisibility(View.VISIBLE);


                YoYo.with(Techniques.RotateIn)
                        .duration(500)
                        .playOn(img);



            }
        }
    }
}
