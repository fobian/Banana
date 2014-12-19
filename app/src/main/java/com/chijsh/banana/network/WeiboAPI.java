package com.chijsh.banana.network;

import com.chijsh.banana.Config;
import com.chijsh.banana.model.FollowsModel;
import com.chijsh.banana.model.PostsModel;
import com.chijsh.banana.model.UserModel;
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

    private WeiboAPI() {
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
    }

    public static WeiboAPI getInstance() {
        if(sAPI == null) {
            sAPI = new WeiboAPI();
        }
        return sAPI;
    }


    public PostsModel getTimeline(String token, long sinceId) {
        return sWeiboService.getTimeline(token, sinceId);

    }

    public void getUserInfo(String token, String screenName, Callback<UserModel> cb) {
        sWeiboService.getUserInfo(token, screenName, cb);
    }

    public UserModel getUserInfo(String token, long uid) {
        return sWeiboService.getUserInfo(token, uid);
    }

    public void postWeibo(String token, String content, Callback<Response> cb) {
        sWeiboService.postWeibo(token, content, cb);
    }

    public void addFavorites(String token, long postId, Callback<Response> cb) {
        sWeiboService.addFavorites(token, postId, cb);
    }

    public void deleteFavorites(String token, long postId, Callback<Response> cb) {
        sWeiboService.deleteFavorites(token, postId, cb);
    }

    public void getFollows(String token, long uid, int count, int cursor, Callback<FollowsModel> cb) {
        sWeiboService.getFollows(token, uid, count, cursor, cb);
    }

    public void getFollowers(String token, long uid, int count, int cursor,Callback<FollowsModel> cb) {
        sWeiboService.getFollowers(token, uid, count, cursor, cb);
    }

}
