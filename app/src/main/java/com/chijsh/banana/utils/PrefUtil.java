package com.chijsh.banana.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chijsh on 11/4/14.
 */
public class PrefUtil {

    public static final String PREF_SINCE_ID = "pref_since_id";

    public static final String PREF_SIGN_OUT = "pref_sign_out";

    public static final String PREF_FIRST_LAUNCH = "pref_first_launch";

    public static final String PREF_MANUALLY_SYNC = "pref_manually_sync";
    

    public static void writeSinceId(Context context, String sinceId) {
        SharedPreferences pref = context.getSharedPreferences(PREF_SINCE_ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(PREF_SINCE_ID, Long.parseLong(sinceId));
        editor.commit();
    }

    public static long readSinceId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_SINCE_ID, Context.MODE_PRIVATE);
        return pref.getLong(PREF_SINCE_ID, 0);
    }

    public static void markFirstLaunch(Context context, boolean isFirst) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FIRST_LAUNCH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_FIRST_LAUNCH, isFirst);
        editor.commit();
    }

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FIRST_LAUNCH, Context.MODE_PRIVATE);
        return pref.getBoolean(PREF_FIRST_LAUNCH, true);
    }

    public static void markManuallySync(Context context, boolean isManually) {
        SharedPreferences pref = context.getSharedPreferences(PREF_MANUALLY_SYNC, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_MANUALLY_SYNC, isManually);
        editor.commit();
    }

    public static boolean isManuallySync(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_MANUALLY_SYNC, Context.MODE_PRIVATE);
        return pref.getBoolean(PREF_MANUALLY_SYNC, false);
    }
}
