package com.chijsh.banana.data.repository.datasource;

import android.content.Context;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.data.cache.PostCache;
import com.chijsh.banana.data.cache.UserCache;
import com.chijsh.banana.data.net.WeiboAPI;

/**
 * Created by chijsh on 3/3/15.
 */
public class TimeLineDataStoreFactory {

    private Context mContext;
    private final PostCache mPostCache;
    private final UserCache mUserCache;

    public TimeLineDataStoreFactory(Context mContext, PostCache mPostCache, UserCache userCache) {
        this.mContext = mContext;
        this.mPostCache = mPostCache;
        this.mUserCache = userCache;
    }

    public TimeLineDataStore createDiskDataStore(String postId) {
        TimeLineDataStore timeLineDataStore;

        if (!mPostCache.isExpired() && mPostCache.isCached(postId)) {
            timeLineDataStore = new DiskTimeLineDataStore(mPostCache);
        } else {
            timeLineDataStore = createCloudDataStore();
        }

        return timeLineDataStore;
    }

    public TimeLineDataStore createCloudDataStore() {
        String token = AccessTokenKeeper.readAccessToken(mContext).getToken();
        WeiboAPI weiboAPI = WeiboAPI.getInstance(token);
        return new CloudTimeLineDataStore(weiboAPI, mPostCache, mUserCache);
    }
}
