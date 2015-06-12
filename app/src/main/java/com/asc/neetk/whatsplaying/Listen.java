package com.asc.neetk.whatsplaying;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by utk994 on 05/04/15.
 */
public class Listen extends Fragment {

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        //setRetainInstance(true); // handle rotations gracefully


    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_listen, container, false);


        return rootView;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        {

            final TextView tv = (TextView) getActivity().findViewById(R.id.listen);
            ImageView iv = (ImageView) getActivity().findViewById(R.id.imageView1);

            if (iv != null)
            {
            iv.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {


                     AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setMessage("Play Songs as you Normally would.WhatsPlayin will Listen for new Songs.");




                    alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            getActivity().startService(new Intent(getActivity(), MyService.class));

                            tv.setVisibility(View.GONE);

                            FragmentTransaction trans = getFragmentManager()
                                    .beginTransaction();

                /*
				 * IMPORTANT: We use the "root frame" defined in
				 * "root_fragment.xml" as the reference to replace fragment
				 */
                             trans.replace(R.id.root_frame, new Listen2());

				/*
				 * IMPORTANT: The following lines allow us to add the fragment
				 * to the stack and return to it later, by pressing back
				 */
                            // trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            // trans.addToBackStack(null);

                            trans.commit();




                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            Notification("What's Playin?", "Listening for new songs.");



                            startActivity(startMain);
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();




                }



                });
            }


        }


    }





    private void Notification(String notificationTitle,
                              String notificationMessage) {
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(
                R.drawable.launcher, "Whats Playing Now Listening",
                System.currentTimeMillis());

        Intent notificationIntent = new Intent(getActivity(), central.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(    getActivity(), 0,
                notificationIntent, 0);

        notification.setLatestEventInfo(getActivity(),
                notificationTitle, notificationMessage, pendingIntent);
        notification.flags |= Notification.FLAG_ONGOING_EVENT;

        notificationManager.notify(0, notification);


    }
}
