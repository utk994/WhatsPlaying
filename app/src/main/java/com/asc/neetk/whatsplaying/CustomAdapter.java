package com.asc.neetk.whatsplaying;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by utk994 on 28/04/15.
 */
public class CustomAdapter extends ExpandableListItemAdapter<Integer> {


    Context context;
    List<RowItem> rowItem;
    DynamicListView list;


    ImageLoader imageLoader;


    CircleImageView img;


    RetrieveArt task;
    String url;


    CustomAdapter(Context context, List<RowItem> rowItem) {
        super(context, R.layout.list_item, R.id.song1, R.id.song2, null);
        this.context = context;
        this.rowItem = rowItem;

    }



    @Override
    public int getCount() {

        return rowItem.size();
    }


    public RowItem getItem1(int position) {

        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {

        return rowItem.indexOf(getItem1(position));
    }


    @NonNull
    @Override
    public View getTitleView(final int position, View convertView, ViewGroup viewGroup) {

        final ViewHolderItem1 viewHolder;

        imageLoader = ImageLoader.getInstance();


        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolderItem1();


            viewHolder.likenum = (TextView) convertView.findViewById(R.id.likenum);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.timestamp = (TextView) convertView
                    .findViewById(R.id.timestamp);
            viewHolder.statusMsg = (TextView) convertView
                    .findViewById(R.id.txtStatusMsg);

            viewHolder.profilepic = (ImageView) convertView
                    .findViewById(R.id.profilePic);

            viewHolder.likes = (ImageView) convertView.findViewById(R.id.likeimg);

            viewHolder.findprofile = (ImageView) convertView.findViewById(R.id.viewprofile);

            convertView.setTag(viewHolder);


        } else {

            // we've just avoided calling findViewById() on resource everytime

            // just use the viewHolder

            viewHolder = (ViewHolderItem1) convertView.getTag();

        }

        Integer like = 0;

        final RowItem row_pos = rowItem.get(position);
        // setting the image resource and title


        viewHolder.findprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, userProfile.class);
                i.putExtra("User", row_pos.getName());
                context.startActivity(i);
            }
        });


        viewHolder.name.setText(row_pos.getName());
        /*CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(row_pos.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS); */

        if (viewHolder.likenum != null && row_pos != null)
            viewHolder.likenum.setText(row_pos.getLikes().toString());


        if (viewHolder.timestamp != null && row_pos != null)
            viewHolder.timestamp.setText(row_pos.getTimeStamp());

        if (row_pos != null) {

            like = row_pos.getLikes();
        }


        if (!TextUtils.isEmpty(row_pos.getSong())) {
            viewHolder.statusMsg.setText(" is listening to " + row_pos.getSong() + " by " + row_pos.getArtist());
            viewHolder.statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            viewHolder.statusMsg.setVisibility(View.GONE);
        }

        viewHolder.profilepic.setImageDrawable(row_pos.getProfilePic());

        //if (like  ==0){ likes.setImageResource(R.drawable.likegra);}
        final Integer finalLike = like;
        viewHolder.likes.setOnClickListener(new View.OnClickListener() {

            Integer templ = finalLike;


            public void onClick(View v) {

                templ = templ + 1;


                viewHolder.likes.setImageResource(R.drawable.like);


                viewHolder.likenum.setText(String.valueOf(templ));

                RowItem temp = getItem1(position);

                String id = temp.getObjID();

                Log.d("Hello1", id);
                ParseObject p = ParseObject.createWithoutData("Songs", id);
                p.put("Likes", templ);
                p.saveInBackground();


            }
        });


        return convertView;
    }

    @NonNull
    @Override
    public View getContentView(final int position, View convertView, @NonNull final ViewGroup viewGroup) {

        final ViewHolderItem2 viewHolder;


        if (convertView == null)


        {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.expnd, null);

            viewHolder = new ViewHolderItem2();

            viewHolder.songname = (TextView) convertView.findViewById(R.id.songname);
            viewHolder.artist1 = (TextView) convertView.findViewById(R.id.artist);
            viewHolder.album1 = (TextView) convertView.findViewById(R.id.album);


            viewHolder.listen = (Button) convertView.findViewById(R.id.listenonline);
            viewHolder.img = (CircleImageView) convertView.findViewById(R.id.coverfor);



            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolderItem2) convertView.getTag();


        }


        final RowItem row_pos = rowItem.get(position);

        /*


        CustomAdapter.this.setExpandCollapseListener(new ExpandCollapseListener() {
            @Override
            public void onItemExpanded(int i) {

                StringBuilder stringBuilder = new StringBuilder("http://ws.audioscrobbler.com/2.0/");
                stringBuilder.append("?method=album.getinfo");
                stringBuilder.append("&api_key=");
                stringBuilder.append("3d4c79881824afd6b4c7544b753d1024");

                RowItem actrow = rowItem.get(i);


                View view = getContentView(i);

                Log.d("Image", img.toString());
                try {
                    stringBuilder.append("&artist=" + URLEncoder.encode(actrow.getArtist(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                try {
                    stringBuilder.append("&album=" + URLEncoder.encode(actrow.getAlbum(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                task = (RetrieveArt) new RetrieveArt().execute(stringBuilder.toString());
                notifyDataSetChanged();

            }

            @Override
            public void onItemCollapsed(int i) {

                task.cancel(true);

            }
        }); */


        viewHolder.songname.setText(row_pos.getSong());
        viewHolder.artist1.setText(row_pos.getArtist());
        viewHolder.album1.setText(row_pos.getAlbum());


        viewHolder.img.setImageDrawable(row_pos.getAlbumart());




        viewHolder.listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new
                        Intent(context, tutsPlayer.class);
                intent.putExtra("Songname", row_pos.getSong());
                intent.putExtra("Artist", row_pos.getArtist());
                intent.putExtra("Album", row_pos.getAlbum());
                intent.putExtra("url",row_pos.getAlbumurl());

                context.startActivity(intent);


            }
        });






        return convertView;


    }


    static class ViewHolderItem1 {
        TextView likenum;
        TextView name;
        TextView timestamp;
        TextView statusMsg;
        ImageView likes;
        ImageView profilepic;
        ImageView findprofile;


    }


    static class ViewHolderItem2 {
        TextView songname;
        TextView artist1;
        TextView album1;
        Button listen;
        CircleImageView img;

    }


}




