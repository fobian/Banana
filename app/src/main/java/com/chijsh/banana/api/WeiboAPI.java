package com.chijsh.banana.api;

import com.chijsh.banana.Config;
import com.chijsh.banana.model.Posts;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by chijsh on 10/20/14.
 */
public class WeiboAPI {

    private static Weibo weibo;
    private static WeiboAPI sAPI;

    interface Weibo {

        @GET("/statuses/friends_timeline.json")
        Posts getHomeTimeLine(@Query("access_token") String token, @Query("since_id") long sinceId);


    }
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

        weibo = restAdapter.create(Weibo.class);
    }
    public static WeiboAPI getInstance() {
        if(sAPI == null) {
            sAPI = new WeiboAPI();
        }
        return sAPI;
    }


    public Posts getHomeLine(String token, long sinceId) {
        return weibo.getHomeTimeLine(token, sinceId);

    }


}
