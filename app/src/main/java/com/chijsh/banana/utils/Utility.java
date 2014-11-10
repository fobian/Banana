package com.chijsh.banana.utils;

import android.content.Context;
import android.os.Build;

import com.chijsh.banana.app.MyApplication;
import com.chijsh.banana.model.PicUrl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by chijsh on 10/28/14.
 */
public class Utility {

    private static final String DATE_FORMAT = "MM-dd HH:mm";

    public static float density = 1;

    public static String[] strToArray(String source) {
        return source.split(",");
    }

    public static String urlsToString(List<PicUrl> urls) {
        String s = "";
        for (int i = 0; i < urls.size(); ++i) {
            s += urls.get(i).toString();
            if(i != urls.size() - 1) {
                s += ",";
            }
        }
        return s;
    }

    public static String getFriendlyDate(String time) {
        //TODO
        Date date = new Date(time);
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeZone(tz);
            calendar.setTime(date);
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            Date currentTimeZone = calendar.getTime();
            return sdf.format(currentTimeZone);
        }catch (Exception e) {
        }
        return "";
    }

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static int dp(int value) {
        return (int)(density * value);
    }

    public static Context getAppContext() {
        return MyApplication.getAppContext();
    }
}
