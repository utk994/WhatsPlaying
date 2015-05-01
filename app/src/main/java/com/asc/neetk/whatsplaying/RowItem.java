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

    public RowItem(String name,String status,String timeStamp,Drawable profilePic ) {

        this.name = name;
        this.status = status;
        this.timeStamp=timeStamp;
        this.profilePic=profilePic;


    }


    public String getName() {
        return name;
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