package com.asc.neetk.whatsplaying;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 * Created by utk994 on 28/04/15.
 */
public class RowItem {


    private String name;
    private String song;
    private String artist;
    private String album;

    private String timeStamp;
    private Drawable profilePic;
    private Integer likes;
    private String objID;
    private Date actdate;



    public RowItem(String name, String song,String artist,String album, String timeStamp, Drawable profilePic, Integer likes, String objID,Date actdate) {

        this.name = name;
        this.song = song;
        this.artist = artist;
        this.album = album;
        this.timeStamp = timeStamp;
        this.profilePic = profilePic;
        this.likes = likes;
        this.objID = objID;
        this.actdate=actdate;



    }

    public Date getActdate() {
        return actdate;
    }

    public String getName() {
        return name;
    }

    public String getSong() {
        return song;
    }
    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }




    public Integer getLikes() {


        return likes;
    }

    public String getObjID() {
        return objID;
    }


    public String getTimeStamp() {
        return timeStamp;
    }

    public Drawable getProfilePic() {
        return profilePic;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setLikes(Integer likes1) {
        this.likes = likes1;
    }


    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setProfilePic(Drawable profilePic) {
        this.profilePic = profilePic;
    }


}