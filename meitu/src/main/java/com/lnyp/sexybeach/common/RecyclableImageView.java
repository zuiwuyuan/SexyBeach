package com.lnyp.sexybeach.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class RecyclableImageView extends ImageView {


    public RecyclableImageView(Context context) {
        super(context);
    }

    public RecyclableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        setImageDrawable(null);
    }
}
