package com.asc.neetk.whatsplaying;

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
import android.widget.AdapterView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by utk994 on 05/04/15.
 */

public class Explore extends SwipeRefreshListFragment implements AdapterView.OnItemClickListener {
    SwipeRefreshLayout mSwipeRefreshLayout;


    String[] songname = new String[30];
    String[] artist = new String[30];
    String[] user = new String[30];
    String[] album = new String[30];

    String[] time = new String[30];
    int size;
    Format formatter = new SimpleDateFormat("HH:mm");


    CustomAdapter adapter;
    private List<RowItem> rowItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_fragment, null, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_container);


        super.onActivityCreated(savedInstanceState);
        getListView().setVisibility(View.INVISIBLE);

        rowItems = fetchData();
        TextView tv =(TextView) getActivity().findViewById(R.id.empty);
        tv.setText("Please Swipe Up to Refresh");

        adapter = new CustomAdapter(getActivity(), rowItems);
        adapter.notifyDataSetChanged();
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        tv.setVisibility(View.INVISIBLE);
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

                getListView().setVisibility(View.VISIBLE);

            }
        });

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

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Songs");
        query.orderByDescending("updatedAt");
        query.setLimit(15);


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


                        time[i] = DateUtils.getRelativeTimeSpanString(p.getCreatedAt().getTime(), System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS).toString();


                    }


                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }


            }
        });

        Drawable drawable = getResources().getDrawable(R.drawable.profile);
        for (int j = 0; j < size; j++) {
            songname[j] = " is listening to " + songname[j] + " by " + artist[j];

        }

        // menutitles = getResources().getStringArray(R.array.titles);


        rowItems = new ArrayList<RowItem>();

        for (int i = 0; i < size; i++) {
            RowItem items = new RowItem(user[i], songname[i], time[i], drawable);

            rowItems.add(items);
        }
        return rowItems;


    }


}
