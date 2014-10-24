package com.chijsh.banana.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by chijsh on 10/24/14.
 */
public class WeiboAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private WeiboAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new WeiboAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
