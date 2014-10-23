package com.chijsh.banana.ui;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.chijsh.banana.api.WeiboAPI;
import com.chijsh.banana.data.PostContract;
import com.chijsh.banana.model.Post;
import com.chijsh.banana.data.PostContract.PostEntry;

import java.util.List;
import java.util.Vector;

/**
 * Created by chijsh on 10/23/14.
 */
public class FetchWeiboTask extends AsyncTask {
    private final String LOG_TAG = FetchWeiboTask.class.getSimpleName();
    private Context mContext;
    private SimpleCursorAdapter mPostAdapter;
    public FetchWeiboTask(Context context, SimpleCursorAdapter postAdapter) {
        mContext = context;
        mPostAdapter = postAdapter;
    }
    private long addPost(Post post) {

        Log.v(LOG_TAG, "inserting " + post.login);

        Cursor cursor = mContext.getContentResolver().query(
                PostEntry.CONTENT_URI,
                new String[]{PostEntry._ID},
                PostEntry.COLUMN_CONTRIBUTOR + " = ?",
                new String[]{post.login},
                null);

        if (cursor.moveToFirst()) {
            Log.v(LOG_TAG, "Found it in the database!");
            int postIdIndex = cursor.getColumnIndex(PostEntry._ID);
            return cursor.getLong(postIdIndex);
        } else {
            Log.v(LOG_TAG, "Didn't find it in the database, inserting now!");
            ContentValues values = new ContentValues();
            values.put(PostEntry.COLUMN_CONTRIBUTOR, post.login);
            values.put(PostEntry.COLUMN_CONTRIBUTIONS, post.contributions);


            Uri insertUri = mContext.getContentResolver()
                    .insert(PostEntry.CONTENT_URI, values);

            return ContentUris.parseId(insertUri);
        }
    }
    @Override
    protected Object doInBackground(Object[] params) {
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
            int rowsInserted = mContext.getContentResolver()
                    .bulkInsert(PostContract.PostEntry.CONTENT_URI, cvArray);
            Log.v(LOG_TAG, "inserted " + rowsInserted + " rows of weather data");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
