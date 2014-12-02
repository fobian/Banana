package com.chijsh.banana.api;

import com.chijsh.banana.Config;
import com.chijsh.banana.model.Follows;
import com.chijsh.banana.model.Posts;
import com.chijsh.banana.model.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

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


    public Posts getHomeLine(String token, long sinceId) {
        return sWeiboService.getHomeTimeLine(token, sinceId);

    }

    public User getUserInfo(String token, String screenName) {
        return sWeiboService.getUserInfo(token, screenName);
    }

    public User getUserInfo(String token, long uid) {
        return sWeiboService.getUserInfo(token, uid);
    }

    public Response postWeibo(String token, String content) {
        return sWeiboService.postWeibo(token, content);
    }

    public Response addFavorites(String token, long postId) {
        return sWeiboService.addFavorites(token, postId);
    }

    public Response deleteFavorites(String token, long postId) {
        return sWeiboService.deleteFavorites(token, postId);
    }

    public Follows getFollows(String token, long uid) {
        return sWeiboService.getFollows(token, uid);
    }

    public Follows getFollowers(String token, long uid) {
        return sWeiboService.getFollowers(token, uid);
    }

}
