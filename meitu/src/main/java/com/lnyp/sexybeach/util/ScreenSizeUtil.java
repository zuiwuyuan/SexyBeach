package com.lnyp.sexybeach.util;

import android.app.Activity;

/**
 * Created by 李宁 on 2016-08-21.
 */
public class ScreenSizeUtil {

    public static int getScreenWidth(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }

}
