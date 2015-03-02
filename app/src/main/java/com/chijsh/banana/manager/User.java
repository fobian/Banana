package com.chijsh.banana.manager;

import com.chijsh.banana.presentation.model.FollowsModel;
import com.chijsh.banana.presentation.model.UserModel;
import com.chijsh.banana.data.net.WeiboAPI;

import retrofit.Callback;

/**
 * Created by chijsh on 12/18/14.
 */
public class User {
    private WeiboAPI mWeiboAPI;
    private String mUserName;

    public User(WeiboAPI weiboAPI, String userName) {
        this.mWeiboAPI = weiboAPI;
        this.mUserName = userName;
    }

    public void getUserInfo(String token, String screenName, Callback<UserModel> cb) {
        mWeiboAPI.getUserInfo(token, screenName, cb);
    }

    public UserModel getUserInfo(String token, long uid) {
        return mWeiboAPI.getUserInfo(token, uid);
    }

    public void getFollows(String token, long uid, int count, int cursor, Callback<FollowsModel> cb) {
        mWeiboAPI.getFollows(token, uid, count, cursor, cb);
    }

    public void getFollowers(String token, long uid,  int count, int cursor, Callback<FollowsModel> cb) {
        mWeiboAPI.getFollowers(token, uid, count, cursor, cb);
    }
}
