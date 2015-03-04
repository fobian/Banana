package com.chijsh.banana.data.cache;

import com.chijsh.banana.data.entity.PostEntity;
import io.realm.Realm;

/**
 * Created by chijsh on 3/3/15.
 */
public class PostCacheImpl implements PostCache {

    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    private Realm mRealm;

    private static PostCacheImpl INSTANCE;

    public static synchronized PostCacheImpl getInstance(Realm mRealm) {
        if (INSTANCE == null) {
            INSTANCE = new PostCacheImpl(mRealm);
        }
        return INSTANCE;
    }

    public PostCacheImpl(Realm mRealm) {
        this.mRealm = mRealm;
    }

    @Override
    public void get(String postId, PostCacheCallback callback) {
        PostEntity entity = mRealm.where(PostEntity.class).equalTo("idstr", postId).findFirst();
        if (entity != null) {
            callback.onPostEntityLoaded(entity);
        } else {
            callback.onError(postId + "not found in cache.");
        }
    }

    @Override
    public void put(PostEntity entity) {
        if (!isCached(entity.getIdstr())) {
//            mRealm.beginTransaction();
//            PostEntity realmPost = mRealm.copyToRealm(entity);
//            mRealm.commitTransaction();
        }
    }

    @Override
    public boolean isCached(String postId) {
        PostEntity entity = mRealm.where(PostEntity.class).equalTo("idstr", postId).findFirst();
        if (entity != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isExpired() {
        return false;
    }


    @Override
    public void evictAll() {

    }
}
