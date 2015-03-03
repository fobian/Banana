package com.chijsh.banana.data.cache;

import com.chijsh.banana.data.entity.PostEntity;

/**
 * Created by chijsh on 3/3/15.
 */
public interface PostCache {

    interface PostCacheCallback {
        void onPostEntityLoaded(PostEntity postEntity);

        void onError(String error);
    }

    void get(String postId, PostCacheCallback callback);

    void put(PostEntity entity);

    boolean isCached(String postId);

    boolean isExpired();

    void evictAll();

}
