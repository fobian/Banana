package com.chijsh.banana.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by chijsh on 11/10/14.
 */
public class ScreenUtil {

    public static int getStatusBarHeight(Activity activity) {
        Rect localRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;

    }

    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPx(int dp) {
        return (int)(dp * getDensity(Utility.getAppContext()) / 160);
    }

    public static int pxToDp(int px) {
        return (int)(px / getDensity(Utility.getAppContext()) * 160);
    }
}
