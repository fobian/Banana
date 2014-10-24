package com.chijsh.banana.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by chijsh on 10/24/14.
 */
public class WeiboSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static WeiboSyncAdapter sWeiboSyncAdapter = null;
    @Override
    public void onCreate() {
        Log.d("WeiboSyncService", "onCreate - WeiboSyncService");
        synchronized (sSyncAdapterLock) {
            if (sWeiboSyncAdapter == null) {
                sWeiboSyncAdapter = new WeiboSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sWeiboSyncAdapter.getSyncAdapterBinder();
    }
}
