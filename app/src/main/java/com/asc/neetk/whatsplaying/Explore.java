package com.asc.neetk.whatsplaying;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by utk994 on 05/04/15.
 */

public class Explore extends SwipeRefreshListFragment implements AdapterView.OnItemClickListener {
    SwipeRefreshLayout mSwipeRefreshLayout;


    String[] songname = new String[60];
    String[] artist = new String[60];
    String[] user = new String[60];
    String[] album = new String[60];
    String [] done = new String[20];

    String[] time = new String[60];
    int size;
    TextView tv;

    Drawable [] profiles = new Drawable[60];




    CustomAdapter adapter;
    private List<RowItem> rowItems;

    @Override
    public void onCreate(Bundle savedState) {

        super.onCreate(savedState);
        setRetainInstance(true); // handle rotations gracefully


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.purple);


        super.onActivityCreated(savedInstanceState);
        getListView().setVisibility(View.INVISIBLE);


        //rowItems = fetchData();
        tv = (TextView) getActivity().findViewById(R.id.empty);
        tv.setText("Please Swipe Up to Refresh");
        tv.setVisibility(View.VISIBLE);

        if (savedInstanceState == null) {
            rowItems=fetchData();
            adapter = new CustomAdapter(getActivity(), rowItems);


        }



        // adapter.notifyDataSetChanged();
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);

        getListView().setVisibility(View.VISIBLE);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListView().setVisibility(View.INVISIBLE);
                mSwipeRefreshLayout.setRefreshing(true);
                rowItems = fetchData();

                adapter = new CustomAdapter(getActivity(), rowItems);
                adapter.notifyDataSetChanged();
                setListAdapter(adapter);

                mSwipeRefreshLayout.setRefreshing(false);
                tv.setVisibility(View.INVISIBLE);

                getListView().setVisibility(View.VISIBLE);

            }
        });

        getListView().setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (getListView() != null && getListView().getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = getListView().getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = getListView().getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
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


        FragmentManager fm = getActivity().getSupportFragmentManager();
        DialogFragment dialog = new SongDialog(); // creating new object
        Bundle args = new Bundle();
        args.putString("SongName", songname[position]);
        args.putString("Album", album[position]);

        args.putString("Artist", artist[position]);
        dialog.setArguments(args);

        dialog.show(getFragmentManager().beginTransaction(), "MyProgressDialog");
    }


    public List<RowItem> fetchData() {
        final Drawable defdrawable = getResources().getDrawable(R.drawable.profile);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Songs");
        query.orderByDescending("updatedAt");
        query.setLimit(20);


        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> Songs, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + Songs.size() + " songs");

                    size = Songs.size();

                    for (int i = 0; i < size; i++) {


                        ParseObject p = Songs.get(i);
                        songname[i] = (p.getString("Track"));
                        artist[i] = (p.getString("Artist"));
                        user[i] = (p.getString("Username"));

                        album[i] = (p.getString("Album"));


                        time[i] = DateUtils.getRelativeTimeSpanString(p.getCreatedAt().getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();




                    }


                } else {

                    Log.d("score", "Error: " + e.getMessage());
                }


            }



        });

        outloop :for (int i=0;i<user.length;i++) {


            for (int j=0;j<i&& user[i]!=null;j++)
            {
                if (user[i].equals(user[j]))
                {profiles[i]=profiles[j];
                continue outloop;}


            }


            if (user[i] != null)

            {
                ParseQuery<ParseUser> query1 = ParseUser.getQuery();
                query1.whereEqualTo("username", user[i]);
                query1.setLimit(1);


                final int finalI = i;
                query1.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {

                            ParseFile user1 = objects.get(0).getParseFile("profilePic");

                            if (user1 != null)

                            {

                                byte[] bitmapdata = new byte[0];
                                try {
                                    bitmapdata = user1.getData();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                Bitmap bitmap1 = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                                Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmap1, 50, 50, true);
                                bitmap1.recycle();

                                Drawable d = new BitmapDrawable(getResources(), bitmapsimplesize);

                                profiles[finalI] = d;
                            } else profiles[finalI] = defdrawable;
                            // Something went wrong.
                        }
                    }
                });
            }
        }







        for (int j = 0; j < size; j++) {
            songname[j] = " is listening to " + songname[j] + " by " + artist[j];

        }

        rowItems =  new ArrayList<RowItem>();

        for (int i = 0; i < size; i++) {
            RowItem items = new RowItem(user[i], songname[i], time[i], profiles[i]);

            rowItems.add(items);
        }
        return rowItems;


    }


}
