package com.chijsh.banana.utils;

import android.util.Log;

import com.chijsh.banana.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chijsh on 12/8/14.
 */
public class DateUtil {

    private static final String TAG = DateUtil.class.getSimpleName();

//    private static int MILL_MIN = 1000 * 60;
//    private static int MILL_HOUR = MILL_MIN * 60;
//    private static int MILL_DAY = MILL_HOUR * 24;

    //"Fri Aug 28 00:00:00 +0800 2009"
    private static final String SINA_DATE_FORMAT = "EEE MMM dd kk:mm:ss z yyyy";

    private static final String DATE_FORMAT = "MM-dd HH:mm";

    private static final String TIME_FORMAT = "HH:mm";

    public static String getFriendlyDate(String time) {
        long postTime = getTimeInMillis(time);
        long now = System.currentTimeMillis();

        Calendar postCalender = Calendar.getInstance();
        postCalender.setTimeInMillis(postTime);

        Calendar nowCalender = Calendar.getInstance();
        nowCalender.setTimeInMillis(now);

        if (isSameDay(nowCalender, postCalender)) {
            return timeInDay(now - postTime);
        }

        if (isYesterday(nowCalender, postCalender)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
            String yesterdayTime = dateFormat.format(postCalender.getTime());
            return Utility.getAppContext().getString(R.string.yesterday) + " " + yesterdayTime;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            String dateTime = dateFormat.format(postCalender.getTime());
            return dateTime;
        }

    }

    public static long getTimeInMillis(String time) {

        DateFormat dateFormat = new SimpleDateFormat(SINA_DATE_FORMAT);
        try {
            Date date = dateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            Log.d(TAG, "Unable parse " + time + "to time.");
            return 0L;
       }
    }

    private static boolean isSameDay(Calendar today, Calendar someDay) {
        return today.get(Calendar.YEAR) == someDay.get(Calendar.YEAR)
                && today.get(Calendar.DAY_OF_YEAR) == someDay.get(Calendar.DAY_OF_YEAR);
    }

    private static boolean isYesterday(Calendar today, Calendar someDay) {
        return today.get(Calendar.YEAR) == someDay.get(Calendar.YEAR)
                && today.get(Calendar.DAY_OF_YEAR) - someDay.get(Calendar.DAY_OF_YEAR) == 1;
    }

    private static String timeInDay(long deltaMillis) {

        long deltaSecond = deltaMillis / 1000;

        if (deltaSecond < 60) {
            return Utility.getAppContext().getString(R.string.just_now);
        }

        long deltaMin = deltaSecond / 60;

        if (deltaMin < 60) {
            return deltaMin + " " + Utility.getAppContext().getString(R.string.mins_ago);
        } else {
            long deltaHour = deltaMin / 60;
            return deltaHour + " " + Utility.getAppContext().getString(R.string.hours_ago);
        }

    }
}
