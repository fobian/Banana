package com.chijsh.banana.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.Utility;
import com.chijsh.banana.api.WeiboAPI;
import com.chijsh.banana.data.PostContract.PostEntry;
import com.chijsh.banana.model.PicUrl;
import com.chijsh.banana.model.Post;
import com.chijsh.banana.model.Posts;

import java.util.List;
import java.util.Vector;


/**
 * Created by chijsh on 10/24/14.
 */
public class WeiboSyncAdapter extends AbstractThreadedSyncAdapter {

    public final String LOG_TAG = WeiboSyncAdapter.class.getSimpleName();

    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public WeiboSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        String token = AccessTokenKeeper.readAccessToken(getContext()).getToken();
        Posts posts = WeiboAPI.getInstance().getHomeLine(token);
        Vector<ContentValues> cVVector = new Vector<ContentValues>(posts.size());
        Post post;
        for (int i = posts.size() - 1; i >= 0; --i) {
            ContentValues values = new ContentValues();
            post = posts.get(i);
            values.put(PostEntry.COLUMN_CREATED_AT, post.createdAt);
            values.put(PostEntry.COLUMN_POST_ID, post.idstr);
            values.put(PostEntry.COLUMN_POST_TEXT, post.text);
            values.put(PostEntry.COLUMN_POST_SOURCE, post.source);
            values.put(PostEntry.COLUMN_POST_FAVORITED, post.favorited);
            if (!post.picUrls.isEmpty())
                values.put(PostEntry.COLUMN_POST_PICURLS, Utility.urlsToString(post.picUrls));//TODO
            if (post.geo != null)
                values.put(PostEntry.COLUMN_POST_GEO, post.geo.toString());
            values.put(PostEntry.COLUMN_USER_ID, post.user.idstr);
            values.put(PostEntry.COLUMN_USER_SCREENNAME, post.user.screenName);
            values.put(PostEntry.COLUMN_USER_AVATAR, post.user.avatarLarge);
            if (post.retweetedStatus != null) {
                values.put(PostEntry.COLUMN_RETWEETED_ID, post.retweetedStatus.idstr);
                if(post.retweetedStatus.user != null)
                    values.put(PostEntry.COLUMN_RETWEETED_USER_SCREENNAME, post.retweetedStatus.user.screenName);
                values.put(PostEntry.COLUMN_RETWEETED_TEXT, post.retweetedStatus.text);
            }
            values.put(PostEntry.COLUMN_REPOST_COUNT, post.repostsCount);
            values.put(PostEntry.COLUMN_COMMENT_COUNT, post.commentsCount);
            values.put(PostEntry.COLUMN_ATTITUDE_COUNT, post.attitudesCount);
            cVVector.add(values);
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            getContext().getContentResolver()
                    .bulkInsert(PostEntry.CONTENT_URI, cvArray);

        }

//        for (int i = 0; i < posts.size(); ++i) {
//            post = posts.get(i);
//            if(post.retweetedStatus != null)
//                Log.d("ssssssssss", post.user.screenName+":"+post.text+"\n"+post.retweetedStatus.user.screenName + ":" + post.retweetedStatus.text + "\n");
//            else
//                Log.d("zzzzzzzzzz", post.user.screenName+":"+post.text+"\n"+ "picurls:" + post.picUrls.size() + "\n");
//        }
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void triggerSyncAdapter(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount,context);

        }
        return newAccount;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        WeiboSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        triggerSyncAdapter(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
