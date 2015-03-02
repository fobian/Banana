package com.chijsh.banana.utils;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.chijsh.banana.MyApplication;

/**
 * Created by chijsh on 10/28/14.
 */
public class Utility {

    public static float density = 1;

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static int dp(int value) {
        return (int)(density * value);
    }

    public static Context getAppContext() {
        return MyApplication.getAppContext();
    }

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

}
