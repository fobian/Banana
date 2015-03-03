package com.chijsh.banana.data.repository.datasource;

import com.chijsh.banana.data.cache.PostCache;
import com.chijsh.banana.data.cache.UserCache;
import com.chijsh.banana.data.entity.PostEntity;
import com.chijsh.banana.data.entity.PostsEntity;
import com.chijsh.banana.data.entity.UserEntity;
import com.chijsh.banana.data.net.WeiboAPI;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by chijsh on 3/3/15.
 */
public class CloudTimeLineDataStore implements TimeLineDataStore {

    private WeiboAPI mAPI;
    private PostCache mPostCache;
    private UserCache mUserCache;

    public CloudTimeLineDataStore(WeiboAPI mAPI, PostCache mPostCache, UserCache mUserCache) {
        this.mAPI = mAPI;
        this.mPostCache = mPostCache;
        this.mUserCache = mUserCache;
    }

    @Override
    public void getTimeLine(long sinceId, final TimeLineCallback postEntityCallback) {
        mAPI.getTimeline(sinceId, new Callback<PostsEntity>() {
            @Override
            public void success(PostsEntity postsEntity, Response response) {
                postEntityCallback.onTimeLineLoaded(postsEntity);
                cachePosts(postsEntity.statuses);
            }

            @Override
            public void failure(RetrofitError error) {
                postEntityCallback.onError(error.getMessage());
            }
        });
    }

    @Override
    public void nextPage() {

    }

    private void cachePosts(List<PostEntity> postEntityList) {
        for (PostEntity entity : postEntityList) {
            mPostCache.put(entity);
            cacheUser(entity.getUser());
        }
    }

    private void cacheUser(UserEntity userEntity) {
        mUserCache.put(userEntity);
    }
}
