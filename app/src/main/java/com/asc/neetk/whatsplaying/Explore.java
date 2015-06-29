package com.asc.neetk.whatsplaying;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gc.materialdesign.views.ButtonFloat;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.dynamicbox.DynamicBox;

/**
 * Created by utk994 on 05/04/15.
 */

public class Explore extends Fragment implements AdapterView.OnItemClickListener {
    SwipeRefreshLayout mSwipeRefreshLayout;


    String[] songname = new String[60];
    String[] artist = new String[60];
    String[] user = new String[60];
    String[] album = new String[60];

    Drawable albumart[] = new Drawable[30];

    int pos;


    RetrieveArt task;

    String[] urls = new String[30];

    Integer[] likes = new Integer[60];
    String[] objId = new String[60];

    Date[] actime = new Date[60];

    String[] actsongname = new String[60];


    String[] time = new String[60];
    int size;
    TextView tv;

    Drawable[] profiles = new Drawable[60];

    RetreiveItems mTask;

    DynamicListView list;


    CustomAdapter adapter;
    SwingLeftInAnimationAdapter animationAdapter;
    private List<RowItem> rowItems;
    ButtonFloat swap;
    DynamicBox box;

    ActionBar mActionBar;

    ImageLoader imageLoader;


    protected FragmentActivity mActivity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }


    @Override

    public void onCreate(Bundle savedState) {


        super.onCreate(savedState);

        Drawable d = getActivity().getResources().getDrawable(R.drawable.albumart);
        Arrays.fill(albumart, d);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment, null, false);


        imageLoader = ImageLoader.getInstance();


        list = (DynamicListView) rootView.findViewById(R.id.list);


        list.setDivider(null);
        list.setDividerHeight(0);


        list.setScrollingCacheEnabled(true);

        swap = (ButtonFloat) rootView.findViewById(R.id.buttonFloat);


        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (task != null) task.cancel(true);

                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();


                trans.replace(R.id.root_exploreframe, new ExploreFreinds());

                trans.commit();


            }
        });


        return rootView;


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();


        mSwipeRefreshLayout = (SwipeRefreshLayout) mActivity.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple);


        super.onActivityCreated(savedInstanceState);
        box = new DynamicBox(mActivity, list);

        box.setLoadingMessage("Loading ...");

        View customView = mActivity.getLayoutInflater().inflate(R.layout.nonet, null, false);
        box.addCustomView(customView, "noNet");


        View loadingView = mActivity.getLayoutInflater().inflate(R.layout.loadinglayout, null, false);
        box.addCustomView(loadingView, "loading");


        if (!(isOnline())) {

            box.showCustomView("noNet");

            Button retry = (Button) mActivity.findViewById(R.id.retry);

            if (retry != null) {
                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTask = (RetreiveItems) new RetreiveItems().execute();

                        box.showCustomView("loading");

                    }
                });
            }

        }

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser.get("genLikes") == null) {
            currentUser.add("genLikes", "");
            ArrayList<String> init = new ArrayList<String>();
            init.add("");

            currentUser.put("genLikes", init);
            currentUser.saveInBackground();
        }


        if (currentUser.get("Follows") == null) {
            ArrayList<String> init = new ArrayList<String>();
            init.add("");

            currentUser.put("Follows", init);
            currentUser.saveInBackground();

        }

        if (currentUser.get("artLikes") == null) {
            ArrayList<String> init = new ArrayList<String>();
            init.add("");

            currentUser.put("artLikes", init);
            currentUser.saveInBackground();
        }
        if (currentUser.get("folo") == null) {
            ArrayList<String> init = new ArrayList<String>();
            init.add("");

            currentUser.put("folo", init);
            currentUser.saveInBackground();
        }


        tv = (TextView) mActivity.findViewById(R.id.empty);

            if (savedInstanceState == null) {
            swap.setVisibility(View.INVISIBLE);
            mTask = (RetreiveItems) new RetreiveItems().execute();
            box.showCustomView("loading");


        }


        list.setOnItemClickListener(this);

        list.setEmptyView(mActivity.findViewById(R.id.empty));


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                mSwipeRefreshLayout.setRefreshing(true);
                swap.setVisibility(View.INVISIBLE);
                mTask = (RetreiveItems) new RetreiveItems().execute();
                box.showCustomView("loading");



                mSwipeRefreshLayout.setRefreshing(false);
                tv.setVisibility(View.GONE);


            }
        });


        list.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


                if (scrollState == SCROLL_STATE_IDLE) {


                    {
                        swap.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.SlideInUp)
                                .duration(300)
                                .playOn(swap);


                    }


                } else {

                    if (!(YoYo.with(Techniques.SlideOutDown)
                            .duration(200)
                            .playOn(swap).isRunning()))


                    {
                        swap.setVisibility(View.INVISIBLE);
                    }


                }
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


                boolean enable = false;
                if (list != null && list.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = list.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = list.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout


                    enable = firstItemVisible && topOfFirstItemVisible;

                    if (enable) mActionBar.show();

                    else mActionBar.hide();

                    mSwipeRefreshLayout.setEnabled(enable);
                    return;
                }


                mSwipeRefreshLayout.setEnabled(true);


            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }


    @Override
    public void onDestroy() {
        if (mTask!=null)   mTask.cancel(true);
        if (task != null) task.cancel(true);

        super.onDestroy();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {


        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.song2);
        View child = getActivity().getLayoutInflater().inflate(R.layout.list_fragment, null, false);
        linearLayout.addView(child);


        android.app.FragmentManager fm = mActivity.getFragmentManager();
        android.app.DialogFragment dialog = new SongDialog(); // creating new object
        Bundle args = new Bundle();

        args.putString("SongName", actsongname[position]);
        args.putString("Album", album[position]);

        args.putString("Artist", artist[position]);
        args.putString("User", user[position]);
        dialog.setArguments(args);


        dialog.show(fm.beginTransaction(), "MyProgressDialog");

    }


    public class RetreiveItems extends AsyncTask<String, Void, List<RowItem>> {


        @Override
        protected List<RowItem> doInBackground(String... urls) {


            if (!(isOnline())) {

                box.showCustomView("noNet");

                Button retry = (Button) mActivity.findViewById(R.id.retry);


                {
                    retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            mTask = (RetreiveItems) new RetreiveItems().execute();
                        }
                    });
                }

            }


            rowItems = new ArrayList<RowItem>();


            final Drawable defdrawable = getResources().getDrawable(R.drawable.profile);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Songs");
            query.orderByDescending("createdAt");
            query.setLimit(30);

            try {

                List<ParseObject> Songs = query.find();

                Log.d("score", "Retrieved " + Songs.size() + " songs");

                size = Songs.size();

                outloop:
                for (int i = 0; i < size; i++) {


                    ParseObject p = Songs.get(i);
                    objId[i] = p.getObjectId();

                    artist[i] = (p.getString("Artist"));
                    user[i] = (p.getString("Username"));

                    album[i] = (p.getString("Album"));

                    likes[i] = (p.getInt("Likes"));


                    actsongname[i] = (p.getString("Track"));

                    songname[i] = " is listening to " + actsongname[i] + " by " + artist[i];


                    time[i] = DateUtils.getRelativeTimeSpanString(p.getCreatedAt().getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();


                    actime[i] = (p.getCreatedAt());

                    for (int j = 0; j < i && user[i] != null; j++) {
                        if (user[i].equals(user[j])) {
                            profiles[i] = profiles[j];

                            RowItem items = new RowItem(user[i], actsongname[i], artist[i], album[i], time[i], profiles[i], likes[i], objId[i], actime[i], albumart[i], "");


                            rowItems.add(items);


                            continue outloop;
                        }
                    }


                    if (user[i] != null)

                    {
                        ParseQuery<ParseUser> query1 = ParseUser.getQuery();
                        query1.whereEqualTo("username", user[i]);
                        query1.setLimit(1);


                        final int finalI = i;
                        try {
                            List<ParseUser> objects = query1.find();

                            ParseFile user1 = objects.get(0).getParseFile("profilePic");

                            if (user1 != null)

                            {

                                byte[] bitmapdata = new byte[0];
                                try {
                                    bitmapdata = user1.getData();
                                } catch (ParseException e1) {

                                    box.showInternetOffLayout();
                                    e1.printStackTrace();
                                }
                                Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                                Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmap1, 50, 50, true);
                                bitmap1.recycle();

                                Drawable d = new BitmapDrawable(getResources(), bitmapsimplesize);

                                profiles[finalI] = d;


                            } else {
                                profiles[finalI] = defdrawable;


                            }


                            RowItem items = new RowItem(user[finalI], actsongname[finalI], artist[finalI], album[finalI], time[finalI], profiles[finalI], likes[finalI], objId[finalI], actime[finalI], albumart[i], "");


                            rowItems.add(items);

                            Collections.sort(rowItems, new Comparator<RowItem>() {
                                @Override
                                public int compare(RowItem o1, RowItem o2) {
                                    if (o1.getActdate() == null || o2.getActdate() == null)
                                        return 0;
                                    return (o1.getActdate()).compareTo(o2.getActdate());
                                }


                            });

                            Collections.reverse(rowItems);

                            if (finalI == size - 1)
                                return rowItems;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                }
            } catch (
                    ParseException e1
                    )

            {
                e1.printStackTrace();
            }


            return rowItems;


        }


        @Override
        protected void onPostExecute(List<RowItem> items) {


            adapter = new CustomAdapter(mActivity, items);

            adapter.notifyDataSetChanged();


            animationAdapter = new SwingLeftInAnimationAdapter(adapter);
            animationAdapter.notifyDataSetChanged();


            if (list != null)

            {
                animationAdapter.setAbsListView(list);


                list.setAdapter(animationAdapter);


                adapter.setExpandCollapseListener(new ExpandableListItemAdapter.ExpandCollapseListener() {
                    @Override
                    public void onItemExpanded(int i) {


                        pos = i;


                        {
                            StringBuilder stringBuilder = new StringBuilder("http://ws.audioscrobbler.com/2.0/");
                            stringBuilder.append("?method=album.getinfo");
                            stringBuilder.append("&api_key=");
                            stringBuilder.append("3d4c79881824afd6b4c7544b753d1024");


                            try {
                                stringBuilder.append("&artist=" + URLEncoder.encode(artist[i], "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }


                            try {
                                stringBuilder.append("&album=" + URLEncoder.encode(album[i], "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            task = (RetrieveArt) new RetrieveArt().execute(stringBuilder.toString());


                            adapter.notifyDataSetChanged();
                            animationAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onItemCollapsed(int i) {
                        task.cancel(true);

                    }
                });


            }

            box.hideAll();


            YoYo.with(Techniques.Wobble)
                    .duration(700)
                    .playOn(getActivity().findViewById(R.id.buttonFloat));
            swap.setVisibility(View.VISIBLE);


            final SharedPreferences prefs = getActivity().getSharedPreferences(
                    "com.asc.neetk.whatsplaying", Context.MODE_PRIVATE);

            Boolean ftime = prefs.getBoolean("ftime", true);

            if (ftime) {
                Intent intent = new Intent(getActivity(), guide.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
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


                Log.d("there", url1);


                urls[pos] = url1;


                imageLoader.loadImage(url1, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Drawable drawable = new BitmapDrawable(getResources(), loadedImage);
                        albumart[pos] = drawable;

                        RowItem items = new RowItem(user[pos], actsongname[pos], artist[pos], album[pos], time[pos], profiles[pos], likes[pos], objId[pos], actime[pos], drawable, url1);

                        rowItems.set(pos, items);

                        Log.d("checkthis", rowItems.get(pos).toString());


                        adapter.notifyDataSetChanged();

                        animationAdapter.notifyDataSetChanged();



                        View abc=  adapter.getContentView(pos);

                        if (abc!=null ) {
                            CircleImageView img = (CircleImageView) abc.findViewById(R.id.coverfor);

                            if (img != null)

                                YoYo.with(Techniques.FadeIn).duration(300).playOn(img);
                        }

                    }


                });


            }




        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}