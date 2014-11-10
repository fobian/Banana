package com.chijsh.banana.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by chijsh on 11/10/14.
 */
public class MyApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return sContext;
    }
}
