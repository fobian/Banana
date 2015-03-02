package com.chijsh.banana.data.net;

import android.util.Log;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by chijsh on 10/23/14.
 */
public class MyErrorHandler implements ErrorHandler {
    private static final String LOG_TAG = MyErrorHandler.class.getSimpleName();
    @Override
    public Throwable handleError(RetrofitError cause) {
        Log.d(LOG_TAG, cause.getMessage());
        Response r = cause.getResponse();
        if (r != null && r.getStatus() == 401) {
            return new UnknownError(cause.toString());
        }
        return cause;
    }
}
