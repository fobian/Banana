package com.chijsh.banana.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chijsh on 11/4/14.
 */
public class PrefUtil {

    public static final String SINCE_ID_PREF = "since_id_pref";

    public static final String PREF_SIGNOUT = "pref_sign_out";

    public static void writeSinceId(Context context, String sinceId) {
        SharedPreferences pref = context.getSharedPreferences(SINCE_ID_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(SINCE_ID_PREF, Long.parseLong(sinceId));
        editor.commit();
    }

    public static long readSinceId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(SINCE_ID_PREF, Context.MODE_PRIVATE);
        return pref.getLong(SINCE_ID_PREF, 0);
    }
}
