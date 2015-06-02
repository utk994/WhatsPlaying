package com.asc.neetk.whatsplaying;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by utk994 on 05/04/15.
 */
public class Listen2 extends Fragment {

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
//        setRetainInstance(true); // handle rotations gracefully



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_listening, container, false);


        return rootView;



    }
    @Override
    public void onViewCreated (View view,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button but = (Button) getActivity().findViewById(R.id.listenbut1);
        final TextView tv = (TextView) getActivity().findViewById(R.id.listening);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tv.setVisibility(View.GONE);



                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                trans.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);

                trans.replace(R.id.root_frame, new Listen());



                trans.commit();


                getActivity().stopService(new Intent(getActivity(),MyService.class));



                NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.cancelAll();



            }
        });


    }}


