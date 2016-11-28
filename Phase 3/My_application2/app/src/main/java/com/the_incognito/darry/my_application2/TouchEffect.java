package com.the_incognito.darry.my_application2;

import android.view.MotionEvent;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
/**
 * Created by darry on 11/27/2016.
 */

public class TouchEffect implements View.OnTouchListener {
    /* (non-Javadoc)
      * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
      */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            Drawable d = v.getBackground();
            d.mutate();
            d.setAlpha(150);
            v.setBackgroundDrawable(d);
        }
        else if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL)
        {
            Drawable d = v.getBackground();
            d.setAlpha(255);
            v.setBackgroundDrawable(d);
        }
        return false;
    }
}
