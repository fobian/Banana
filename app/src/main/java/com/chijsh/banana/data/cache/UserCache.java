package com.chijsh.banana.data.cache;

import com.chijsh.banana.data.entity.UserEntity;

/**
 * Created by chijsh on 3/3/15.
 */
public interface UserCache {

    interface UserCacheCallback {
        void onUserEntityLoaded(UserEntity userEntity);

        void onError(String error);
    }

    void get(String userId, UserCacheCallback callback);

    void put(UserEntity entity);

    boolean isCached(String userId);

    boolean isExpired();

    void evictAll();
}
