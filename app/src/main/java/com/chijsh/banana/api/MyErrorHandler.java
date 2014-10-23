package com.chijsh.banana.api;

import android.util.Log;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * Created by chijsh on 10/23/14.
 */
public class MyErrorHandler implements ErrorHandler {
    private static final String LOG_TAG = MyErrorHandler.class.getSimpleName();
    @Override
    public Throwable handleError(RetrofitError cause) {
        Log.d(LOG_TAG, cause.getMessage());
        return null;
    }
}
