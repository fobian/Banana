package com.chijsh.banana.network;

import com.chijsh.banana.model.FollowsModel;
import com.chijsh.banana.model.PostsModel;
import com.chijsh.banana.model.UserModel;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by chijsh on 11/20/14.
 */
public interface WeiboService {

    @GET("/statuses/friends_timeline.json")
    PostsModel getTimeline(@Query("access_token") String token, @Query("since_id") long sinceId);

    @GET("/users/show.json")
    void getUserInfo(@Query("access_token") String token, @Query("screen_name") String screenName, Callback<UserModel> cb);

    @GET("/users/show.json")
    UserModel getUserInfo(@Query("access_token") String token, @Query("uid") long uid);

    @POST("/statuses/update.json")
    void postWeibo(@Query("access_token") String token, @Query("status") String content, Callback<Response> cb);

    @POST("/favorites/create.json")
    void addFavorites(@Query("access_token") String token, @Query("id") long postId, Callback<Response> cb);

    @POST("/favorites/destroy.json")
    void deleteFavorites(@Query("access_token") String token, @Query("id") long postId, Callback<Response> cb);

    @GET("/friendships/friends.json")
    void getFollows(@Query("access_token") String token, @Query("uid") long id, Callback<FollowsModel> cb);

    @GET("/friendships/followers.json")
    void getFollowers(@Query("access_token") String token, @Query("uid") long id, Callback<FollowsModel> cb);
}