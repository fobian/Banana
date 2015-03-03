package com.chijsh.banana.data.net;

import com.chijsh.banana.Config;
import com.chijsh.banana.data.entity.PostsEntity;
import com.chijsh.banana.presentation.model.FollowsModel;
import com.chijsh.banana.presentation.model.PostsModel;
import com.chijsh.banana.presentation.model.UserModel;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by chijsh on 10/20/14.
 */
public class WeiboAPI {

    private static WeiboService sWeiboService;
    private static WeiboAPI sAPI;
    private String mToken;

    private WeiboAPI(String token) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gson))
                .setErrorHandler(new MyErrorHandler())
                .build();

        sWeiboService = restAdapter.create(WeiboService.class);
        mToken = token;
    }

    public static WeiboAPI getInstance(String token) {
        if(sAPI == null) {
            sAPI = new WeiboAPI(token);
        }
        return sAPI;
    }


    public PostsModel getTimeline(long sinceId, Callback<PostsEntity> cb) {
        return sWeiboService.getTimeline(mToken, sinceId, cb);

    }

    public void getUserInfo(String screenName, Callback<UserModel> cb) {
        sWeiboService.getUserInfo(mToken, screenName, cb);
    }

    public UserModel getUserInfo(long uid) {
        return sWeiboService.getUserInfo(mToken, uid);
    }

    public void postWeibo(String content, Callback<Response> cb) {
        sWeiboService.postWeibo(mToken, content, cb);
    }

    public void addFavorites(long postId, Callback<Response> cb) {
        sWeiboService.addFavorites(mToken, postId, cb);
    }

    public void deleteFavorites(long postId, Callback<Response> cb) {
        sWeiboService.deleteFavorites(mToken, postId, cb);
    }

    public void getFollows(long uid, int count, int cursor, Callback<FollowsModel> cb) {
        sWeiboService.getFollows(mToken, uid, count, cursor, cb);
    }

    public void getFollowers(long uid, int count, int cursor,Callback<FollowsModel> cb) {
        sWeiboService.getFollowers(mToken, uid, count, cursor, cb);
    }

}
