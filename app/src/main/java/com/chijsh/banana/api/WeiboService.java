package com.chijsh.banana.api;

import com.chijsh.banana.model.Follows;
import com.chijsh.banana.model.Posts;
import com.chijsh.banana.model.User;

import java.util.List;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by chijsh on 11/20/14.
 */
public interface WeiboService {

    @GET("/statuses/friends_timeline.json")
    Posts getHomeTimeLine(@Query("access_token") String token, @Query("since_id") long sinceId);

    @GET("/users/show.json")
    User getUserInfo(@Query("access_token") String token, @Query("screen_name") String screenName);

    @GET("/users/show.json")
    User getUserInfo(@Query("access_token") String token, @Query("uid") long uid);

    @POST("/statuses/update.json")
    Response postWeibo(@Query("access_token") String token, @Query("status") String content);

    @POST("/favorites/create.json")
    Response addFavorites(@Query("access_token") String token, @Query("id") long postId);

    @POST("/favorites/destroy.json")
    Response deleteFavorites(@Query("access_token") String token, @Query("id") long postId);

    @GET("/friendships/friends.json")
    Follows getFollows(@Query("access_token") String token, @Query("uid") long id);
}
