package com.chijsh.banana.utils;


import android.app.Activity;
import android.graphics.Rect;

/**
 * Created by chijsh on 11/10/14.
 */
public class ScreenUtil {

    public static int getStatusBarHeight(Activity activity) {
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }
}
