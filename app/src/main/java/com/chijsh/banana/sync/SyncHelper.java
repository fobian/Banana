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
import com.chijsh.banana.model.User;
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
        syncTimeLine();
    }

    private void syncTimeLine() {
        String token = AccessTokenKeeper.readAccessToken(mContext).getToken();
        Posts posts = WeiboAPI.getInstance().getHomeLine(token, PrefUtil.readSinceId(mContext));
        Vector<ContentValues> postVector = new Vector<ContentValues>(posts.size());
        Vector<ContentValues> userVector = new Vector<ContentValues>();

        Post post;
        User user;

        for (int i = posts.size() - 1; i >= 0; --i) {

            post = posts.get(i);
            postVector.add(createPostValues(post));
            user = post.user;
            if(!userVector.contains(user)) {
                userVector.add(createUserValues(user));
            }


            if(i == posts.size() - 1) {
                PrefUtil.writeSinceId(mContext, post.idstr);
            }

        }
        if (postVector.size() > 0) {
            ContentValues[] postArray = new ContentValues[postVector.size()];
            postVector.toArray(postArray);
            mContext.getContentResolver()
                    .bulkInsert(PostContract.PostEntry.CONTENT_URI, postArray);
        }

        if(userVector.size() > 0) {
            ContentValues[] userArray = new ContentValues[userVector.size()];
            userVector.toArray(userArray);
            mContext.getContentResolver()
                    .bulkInsert(PostContract.UserEntry.CONTENT_URI, userArray);
        }
    }

    private ContentValues createPostValues(Post post) {

        ContentValues values = new ContentValues();
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

        return values;
    }

    private ContentValues createUserValues(User user) {

        ContentValues values = new ContentValues();
        values.put(PostContract.UserEntry.COLUMN_USER_ID, user.idstr);
        values.put(PostContract.UserEntry.COLUMN_SCREEN_NAME, user.screenName);
        values.put(PostContract.UserEntry.COLUMN_PROVINCE, user.province);
        values.put(PostContract.UserEntry.COLUMN_CITY, user.city);
        values.put(PostContract.UserEntry.COLUMN_LOCATION, user.location);
        values.put(PostContract.UserEntry.COLUMN_DESCRIPTION, user.description);
        values.put(PostContract.UserEntry.COLUMN_URL, user.url);
        values.put(PostContract.UserEntry.COLUMN_PROFILE_URL, user.profileImageUrl);
        values.put(PostContract.UserEntry.COLUMN_GENDER, user.gender);
        values.put(PostContract.UserEntry.COLUMN_FOLLOWERS_COUNT, user.followersCount);
        values.put(PostContract.UserEntry.COLUMN_FRIENDS_COUNT, user.friendsCount);
        values.put(PostContract.UserEntry.COLUMN_STATUSES_COUNT, user.statusesCount);
        values.put(PostContract.UserEntry.COLUMN_FAVOURITES_COUNT, user.favouritesCount);
        values.put(PostContract.UserEntry.COLUMN_CREATED_AT, user.createdAt);
        values.put(PostContract.UserEntry.COLUMN_FOLLOWING, user.following);
        values.put(PostContract.UserEntry.COLUMN_AVATAR_LARGE, user.avatarLarge);
        values.put(PostContract.UserEntry.COLUMN_FOLLOW_ME, user.followMe);

        return values;
    }

}
