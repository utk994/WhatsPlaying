package com.asc.neetk.whatsplaying;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by utk994 on 26/05/15.
 */
public class YourWrapper
{
    private View base;
    private ImageView img;

    public YourWrapper(View base)
    {
        this.base = base;
    }

    public ImageView getButton()
    {
        if (img == null)
        {
            img = (ImageView) base.findViewById(R.id.likeimg);
        }
        return (img);
    }
}