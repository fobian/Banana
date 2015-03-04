package com.chijsh.banana.data.cache;

import com.chijsh.banana.data.entity.UserEntity;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by chijsh on 3/3/15.
 */
public class UserCacheImpl implements UserCache {

    private static final long EXPIRATION_TIME = 5 * 24 * 60 * 60 * 1000;
    private Realm mRealm;

    private static UserCacheImpl INSTANCE;

    public static synchronized UserCacheImpl getInstance(Realm mRealm) {
        if (INSTANCE == null) {
            INSTANCE = new UserCacheImpl(mRealm);
        }
        return INSTANCE;
    }

    public UserCacheImpl(Realm mRealm) {
        this.mRealm = mRealm;
    }

    @Override
    public void get(String userId, UserCacheCallback callback) {
        UserEntity entity = mRealm.where(UserEntity.class).equalTo("idstr", userId).findFirst();
        if (entity != null) {
            callback.onUserEntityLoaded(entity);
        } else {
            callback.onError(userId + "not found in cache.");
        }
    }

    @Override
    public void put(UserEntity entity) {
        if (!isCached(entity.getIdstr())) {
            mRealm.beginTransaction();
            UserEntity realmUser = mRealm.copyToRealm(entity);
            mRealm.commitTransaction();
        }

    }

    @Override
    public boolean isCached(String userId) {
        UserEntity entity = mRealm.where(UserEntity.class).equalTo("idstr", userId).findFirst();
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
        RealmResults<UserEntity> result = mRealm.where(UserEntity.class).findAll();
        result.clear();
    }
}
