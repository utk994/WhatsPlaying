package com.asc.neetk.whatsplaying;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gc.materialdesign.views.ButtonFloat;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.nhaarman.listviewanimations.appearance.simple.SwingLeftInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

    Integer[] likes = new Integer[60];
    String[] objId = new String[60];

    Date[] actime = new Date[60];

    String[] actsongname = new String[60];


    String[] time = new String[60];
    int size;
    TextView tv;

    Drawable[] profiles = new Drawable[60];

    private RetreiveItems mTask;

    DynamicListView list;
    ShowcaseView sv;


    CustomAdapter adapter;
    SwingLeftInAnimationAdapter animationAdapter;
    private List<RowItem> rowItems;
    ButtonFloat swap;
    DynamicBox box;
    Calendar c;
    int seconds;

    ActionBar mActionBar;


    protected FragmentActivity mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
    }


    @Override

    public void onCreate(Bundle savedState) {


        super.onCreate(savedState);

        //   setRetainInstance(true); // handle rotations gracefully
        c = Calendar.getInstance();
        seconds = c.get(Calendar.MILLISECOND);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_fragment, null, false);

        list =(DynamicListView) rootView.findViewById(R.id.list);
        swap = (ButtonFloat) rootView.findViewById(R.id.buttonFloat);





        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        loop1:
        while (true) {
            if (mActivity != null)
                break loop1;
        }

        mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();


        mSwipeRefreshLayout = (SwipeRefreshLayout) mActivity.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple);


        super.onActivityCreated(savedInstanceState);
        box = new DynamicBox(mActivity,list);

        box.setLoadingMessage("Loading ...");

        View customView = mActivity.getLayoutInflater().inflate(R.layout.nonet, null, false);
        box.addCustomView(customView, "noNet");


        if (!(isOnline())) {

            box.showCustomView("noNet");

            Button retry = (Button) mActivity.findViewById(R.id.retry);

            if (retry != null) {
                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTask = (RetreiveItems) new RetreiveItems().execute();
                        box.showLoadingLayout();

                    }
                });
            }

        }

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser.get("genLikes") == null) {
            currentUser.add("genLikes", "");
        }


        if (currentUser.get("Follows") == null) {
            currentUser.add("Follows", "");
        }

        if (currentUser.get("artLikes") == null) {
            currentUser.add("artLikes", "");
        }


        tv = (TextView) mActivity.findViewById(R.id.empty);

        if (savedInstanceState == null) {
            mTask = (RetreiveItems) new RetreiveItems().execute();
            box.showLoadingLayout();


        }


        list.setOnItemClickListener(this);

        list.setEmptyView(mActivity.findViewById(R.id.empty));


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                mSwipeRefreshLayout.setRefreshing(true);
                mTask = (RetreiveItems) new RetreiveItems().execute();
                box.showLoadingLayout();


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
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {


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
            swap.setVisibility(View.INVISIBLE);



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

                        for (int j=0;j<i&& user[i]!=null;j++)
                        {
                            if (user[i].equals(user[j]))
                            {profiles[i]=profiles[j];

                                RowItem items = new RowItem(user[i], songname[i], time[i], profiles[i], likes[i], objId[i], actime[i]);


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



                            RowItem items = new RowItem(user[finalI], songname[finalI], time[finalI], profiles[finalI], likes[finalI], objId[finalI], actime[finalI]);


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

                            if (finalI == size-1)
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

            {animationAdapter.setAbsListView(list);


            list.setAdapter(animationAdapter);


               }
            box.hideAll();


            YoYo.with(Techniques.Wobble)
                    .duration(700)
                    .playOn(getActivity().findViewById(R.id.buttonFloat));
            swap.setVisibility(View.VISIBLE);


        }


    }



    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}