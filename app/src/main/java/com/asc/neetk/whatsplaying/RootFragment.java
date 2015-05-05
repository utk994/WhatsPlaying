package com.asc.neetk.whatsplaying;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by utk994 on 20/04/15.
 */
public class RootFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "RootFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.activity_central, container, false);

        android.support.v4.app.FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();

        boolean run = isMyServiceRunning(MyService.class);

        if (run)
		         transaction.replace(R.id.root_frame, new Listen2());

        else
            transaction.replace(R.id.root_frame, new Listen());

        transaction.commit();

        return view;
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

