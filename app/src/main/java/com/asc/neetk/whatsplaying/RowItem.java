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

    private Drawable albumart;


    private String albumurl;



    public RowItem(String name, String song,String artist,String album, String timeStamp, Drawable profilePic, Integer likes, String objID,Date actdate,Drawable albumart,String albumurl) {

        this.name = name;
        this.song = song;
        this.artist = artist;
        this.album = album;
        this.timeStamp = timeStamp;
        this.profilePic = profilePic;
        this.likes = likes;
        this.objID = objID;
        this.actdate=actdate;
        this.albumart=albumart;

        this.albumurl=albumurl;



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
    public String getAlbumurl() {
        return albumurl;
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


    public Drawable getAlbumart() {
        return albumart;
    }


    public void setName(String name) {
        this.name = name;
    }


    public void setAlbumurl(String albumurl) {
        this.albumurl = albumurl;
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


    public void setAlbumart(Drawable albumart) {
        this.albumart = albumart;
    }


}