package com.asc.neetk.whatsplaying;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by utk994 on 29/05/15.
 */
public class ExploreRoot  extends android.support.v4.app.Fragment {

    private static final String TAG = "ExploreRootFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.activity_explore, container, false);

        android.support.v4.app.FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();


        transaction.replace(R.id.root_exploreframe, new Explore());


        transaction.commit();

        return view;
    }

}

