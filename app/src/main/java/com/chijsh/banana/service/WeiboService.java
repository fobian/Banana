package com.chijsh.banana.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.chijsh.banana.api.WeiboAPI;
import com.chijsh.banana.data.PostContract;
import com.chijsh.banana.model.Post;

import java.util.List;
import java.util.Vector;

/**
 * Created by chijsh on 10/23/14.
 */
public class WeiboService extends IntentService {
    private static final String LOG_TAG = WeiboService.class.getSimpleName();
    public WeiboService() {
        super("WeiboService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Post> posts = WeiboAPI.getInstance().getContributors();
        Vector<ContentValues> cVVector = new Vector<ContentValues>(posts.size());
        Post post;
        for (int i = 0; i < posts.size(); ++i) {
            ContentValues values = new ContentValues();
            post = posts.get(i);
            values.put(PostContract.PostEntry.COLUMN_CONTRIBUTOR, post.login);
            values.put(PostContract.PostEntry.COLUMN_CONTRIBUTIONS, post.contributions);
            cVVector.add(values);
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int rowsInserted = getContentResolver()
                    .bulkInsert(PostContract.PostEntry.CONTENT_URI, cvArray);
            Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of weather data");
        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        public AlarmReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent sendIntent = new Intent(context, WeiboService.class);
            context.startService(sendIntent);

        }
    }
}
