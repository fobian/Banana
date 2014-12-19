package com.chijsh.banana.manager;

import com.chijsh.banana.network.WeiboAPI;

import retrofit.Callback;
import retrofit.client.Response;

/**
 * Created by chijsh on 12/18/14.
 */
public class Weibor {
    private WeiboAPI mWeiboAPI;

    public Weibor(WeiboAPI weiboAPI) {
        this.mWeiboAPI = weiboAPI;
    }

    public void postWeibo(String token, String content, Callback<Response> cb) {
        mWeiboAPI.postWeibo(token, content, cb);
    }

    public void addFavorites(String token, long postId, Callback<Response> cb) {
        mWeiboAPI.addFavorites(token, postId, cb);
    }

    public void deleteFavorites(String token, long postId, Callback<Response> cb) {
        mWeiboAPI.deleteFavorites(token, postId, cb);
    }
}
