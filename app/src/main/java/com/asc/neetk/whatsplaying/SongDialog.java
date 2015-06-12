package com.asc.neetk.whatsplaying;

import android.app.DialogFragment;
import android.content.Intent;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by utk994 on 30/04/15.
 */


public class SongDialog extends DialogFragment {


    public SongDialog() {
        // Empty constructor required for DialogFragment
    }

    View mView;
    String fileName = "Somename.png";


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


    }

    String url;
    String album1;
    String artist1;
    String track1;

    RetrieveArt task;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mView = view;


        Bundle bundle = this.getArguments();

        track1 = bundle.getString("SongName");
        artist1 = bundle.getString("Artist");
        album1 = bundle.getString("Album");
        final String user1 = bundle.getString("User");


        final TextView user = (TextView) view.findViewById(R.id.sharedby);
        final TextView songname = (TextView) view.findViewById(R.id.songname);
        final TextView artist = (TextView) view.findViewById(R.id.artist);
        final TextView album = (TextView) view.findViewById(R.id.album);


        img = (CircleImageView) mView.findViewById(R.id.coverfor);
        img.setImageDrawable(getResources().getDrawable(R.drawable.albumart));


        songname.setText(track1);
        artist.setText(artist1);
        album.setText(album1);
        user.setText(user1);


        Button listen = (Button) view.findViewById(R.id.listenonline);

        listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new
                        Intent(getActivity().getApplicationContext(), tutsPlayer.class);
                intent.putExtra("Songname", track1);
                intent.putExtra("Artist", artist1);
                intent.putExtra("Album", album1);
                intent.putExtra("picname", fileName);
                intent.putExtra("url", url);
                startActivity(intent);
                /*String acttrck = track1.replace(" ", "%20");
                String actart = artist1.replace(" ", "%20");

                final String url = "https://soundcloud.com/search/sounds?q=" + acttrck + "%20" + actart;


                final Intent browserIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
                startActivity(browserIntent); */


            }
        });


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


        task = (RetrieveArt) new RetrieveArt().execute(stringBuilder.toString());

        Log.d("TAG", "onViewCreated");


        return view;
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        task.cancel(true);


    }

    CircleImageView img;




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


        protected void onPostExecute(String url1) {

            url = url1;


            if (url1 != null && !url1.equals("")) {

                Picasso.with(getActivity().getApplicationContext())
                        .load(url1)
                        .fit()
                        .noFade()
                        .into(img, new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {


                                YoYo.with(Techniques.RotateIn)
                                        .duration(300)
                                        .playOn(img);


                            }


                        });


            }
        }

    }
}
