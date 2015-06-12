package com.asc.neetk.whatsplaying;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

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
    public RowItem getItem(int position) {

        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {

        return rowItem.indexOf(getItem(position));
    }


    public void remove(int position) {
        rowItem.remove(position);


    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item, null);
        }
        Integer like=0;

        final RowItem row_pos = rowItem.get(position);
        // setting the image resource and title
        final ImageView likes = (ImageView) convertView.findViewById(R.id.likeimg);
        final TextView likenum = (TextView) convertView.findViewById(R.id.likenum);


        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView
                .findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView
                .findViewById(R.id.txtStatusMsg);

        ImageView profilePic = (ImageView) convertView
                .findViewById(R.id.profilePic);


        name.setText(row_pos.getName());
        /*CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(row_pos.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS); */

        if (likenum != null && row_pos !=null) likenum.setText(row_pos.getLikes().toString());


        if (timestamp!=null&& row_pos!=null)timestamp.setText(row_pos.getTimeStamp());

        if (row_pos!=null){

          like = row_pos.getLikes();}


        if (!TextUtils.isEmpty(row_pos.getStatus())) {
            statusMsg.setText(row_pos.getStatus());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        profilePic.setImageDrawable(row_pos.getProfilePic());

        //if (like  ==0){ likes.setImageResource(R.drawable.likegra);}
        final Integer finalLike = like;
        likes.setOnClickListener(new View.OnClickListener() {

            Integer templ = finalLike;


            public void onClick(View v) {

                templ = templ + 1;


                likes.setImageResource(R.drawable.like);


                likenum.setText(String.valueOf(templ));

                RowItem temp = getItem(position);

                String id = temp.getObjID();

                Log.d("Hello1", id);
                ParseObject p = ParseObject.createWithoutData("Songs", id);
                p.put("Likes", templ);
                p.saveInBackground();


            }
        });


        return convertView;

    }

}
