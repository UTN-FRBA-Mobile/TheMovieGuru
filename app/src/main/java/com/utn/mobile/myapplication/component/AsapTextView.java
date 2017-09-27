package com.utn.mobile.myapplication.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by adro on 18/08/16.
 */
public class AsapTextView extends TextView {



    public AsapTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Asap-Regular_0.otf"));
    }

    @Override
    public void setTypeface(Typeface tf) {
        setTypeface(tf, tf.getStyle());
    }

    @Override
    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(create("fonts/Asap-Bold_0.otf"));
        } else if (style == Typeface.ITALIC) {
            super.setTypeface(create("fonts/Asap-Italic_0.otf"));
        } else {
            super.setTypeface(create("fonts/Asap-Regular_0.otf"));
        }
    }

    private Typeface create(String path) {
        return Typeface.createFromAsset(getContext().getAssets(), path);
    }
}