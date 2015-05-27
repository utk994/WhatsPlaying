package com.asc.neetk.whatsplaying;

import android.graphics.drawable.Drawable;

/**
 * Created by utk994 on 28/04/15.
 */
public class RowItem {


    private String name;
    private String status;
    private String timeStamp;
    private Drawable profilePic;
    private Integer likes;
    private String objID;

                   public RowItem(String name,String status,String timeStamp,Drawable profilePic,Integer likes,String objID) {

        this.name = name;
        this.status = status;
        this.timeStamp=timeStamp;
        this.profilePic=profilePic;
        this.likes=likes;
        this.objID=objID;


    }



    public String getName() {
        return name;
    }
    public Integer getLikes() {


        return likes;
    }

    public String getObjID() {
        return objID;
    }
    public String getStatus() {
        return  status;
    }
    public String getTimeStamp() {
        return  timeStamp;
    }
    public Drawable getProfilePic() {
        return  profilePic;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setLikes(Integer likes1) {
        this.likes= likes1;
    }



    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setProfilePic(Drawable profilePic) {
        this.profilePic = profilePic;
    }



}