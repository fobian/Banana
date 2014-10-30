package com.chijsh.banana;

import android.os.Build;
import android.text.format.DateFormat;

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

    public static String[] strToArray(String source) {
        return source.split(",");
    }

    public static String arrayToStr(List<String> list) {
        int size = list.size();
        String str = "";
        for (int i = 0; i < size; ++i) {
            str += ",";
            str += list.get(i);
        }
        return str;
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
}
