package com.asc.neetk.whatsplaying;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by utk994 on 28/04/15.
 */
public class CustomAdapter extends BaseAdapter {

    Context context;
    List<RowItem> rowItem;

    CustomAdapter(Context context, List<RowItem> rowItem) {
        this.context = context;
        this.rowItem = rowItem;

    }

    @Override
    public int getCount() {

        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {

        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {

        return rowItem.indexOf(getItem(position));
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);}


            final ImageView likes = (ImageView) convertView.findViewById(R.id.likeimg);
            final TextView likenum =(TextView) convertView.findViewById(R.id.likenum);
            final int[] like = {Integer.parseInt(likenum.getText().toString())};
        final RowItem row_pos = rowItem.get(position);
        // setting the image resource and title


        if (like[0] ==0) likes.setImageResource(R.drawable.likegra);
            likes.setOnClickListener(new View.OnClickListener() {


                public void onClick(View v) {

                    like[0] = like[0] + 1;


                    likes.setImageResource(R.drawable.like);


                    likenum.setText(String.valueOf(like[0]));


                    row_pos.setLikes(like[0]);
                }
            });


        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);

         ImageView profilePic = ( ImageView) convertView
                .findViewById(R.id.profilePic);





        name.setText(row_pos.getName());
        /*CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(row_pos.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS); */

            likenum.setText(row_pos.getLikes().toString());


        timestamp.setText(row_pos.getTimeStamp());

        if (!TextUtils.isEmpty(row_pos.getStatus())) {
            statusMsg.setText(row_pos.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        profilePic.setImageDrawable(row_pos.getProfilePic());



        return convertView;

    }

}
