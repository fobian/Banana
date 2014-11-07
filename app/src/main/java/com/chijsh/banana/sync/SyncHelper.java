package com.chijsh.banana.sync;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.api.WeiboAPI;
import com.chijsh.banana.data.PostContract;
import com.chijsh.banana.model.Post;
import com.chijsh.banana.model.Posts;
import com.chijsh.banana.utils.PrefUtil;
import com.chijsh.banana.utils.Utility;

import java.util.Vector;

/**
 * Created by chijsh on 11/4/14.
 */
public class SyncHelper {

    private Context mContext;

    public SyncHelper(Context context) {
        mContext = context;
    }

    public void performSync(SyncResult syncResult, Account account, Bundle extras) {
        timeLineSync();
    }

    private void timeLineSync() {
        String token = AccessTokenKeeper.readAccessToken(mContext).getToken();
        Posts posts = WeiboAPI.getInstance().getHomeLine(token, PrefUtil.readSinceId(mContext));
        Vector<ContentValues> cVVector = new Vector<ContentValues>(posts.size());
        Post post;
        for (int i = posts.size() - 1; i >= 0; --i) {
            ContentValues values = new ContentValues();
            post = posts.get(i);
            values.put(PostContract.PostEntry.COLUMN_CREATED_AT, post.createdAt);
            values.put(PostContract.PostEntry.COLUMN_POST_ID, post.idstr);
            values.put(PostContract.PostEntry.COLUMN_POST_TEXT, post.text);
            values.put(PostContract.PostEntry.COLUMN_POST_SOURCE, post.source);
            values.put(PostContract.PostEntry.COLUMN_POST_FAVORITED, post.favorited);
            if (!post.picUrls.isEmpty())
                values.put(PostContract.PostEntry.COLUMN_POST_PICURLS, Utility.urlsToString(post.picUrls));//TODO
            if (post.geo != null)
                values.put(PostContract.PostEntry.COLUMN_POST_GEO, post.geo.toString());
            values.put(PostContract.PostEntry.COLUMN_USER_ID, post.user.idstr);
            values.put(PostContract.PostEntry.COLUMN_USER_SCREENNAME, post.user.screenName);
            values.put(PostContract.PostEntry.COLUMN_USER_AVATAR, post.user.avatarLarge);
            if (post.retweetedStatus != null) {
                values.put(PostContract.PostEntry.COLUMN_RETWEETED_ID, post.retweetedStatus.idstr);
                if(post.retweetedStatus.user != null)
                    values.put(PostContract.PostEntry.COLUMN_RETWEETED_USER_SCREENNAME, post.retweetedStatus.user.screenName);
                values.put(PostContract.PostEntry.COLUMN_RETWEETED_TEXT, post.retweetedStatus.text);
                values.put(PostContract.PostEntry.COLUMN_RETWEETED_PICURLS, Utility.urlsToString(post.retweetedStatus.picUrls));
            }
            values.put(PostContract.PostEntry.COLUMN_REPOST_COUNT, post.repostsCount);
            values.put(PostContract.PostEntry.COLUMN_COMMENT_COUNT, post.commentsCount);
            values.put(PostContract.PostEntry.COLUMN_ATTITUDE_COUNT, post.attitudesCount);
            cVVector.add(values);

            if(i == posts.size() - 1) {
                PrefUtil.writeSinceId(mContext, post.idstr);
            }

        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            mContext.getContentResolver()
                    .bulkInsert(PostContract.PostEntry.CONTENT_URI, cvArray);

        }
    }

}
