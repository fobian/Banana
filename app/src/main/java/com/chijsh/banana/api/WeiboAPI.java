package com.chijsh.banana.api;


import com.chijsh.banana.model.Post;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by chijsh on 10/20/14.
 */
public class WeiboAPI {


    private static GitHub gitHub;
    private static WeiboAPI sAPI;
    private WeiboAPI() {
        // Create a very simple REST adapter which points the GitHub API endpoint.
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setErrorHandler(new MyErrorHandler())
                .build();

        // Create an instance of our GitHub API interface.
        gitHub = restAdapter.create(GitHub.class);
    }
    public static WeiboAPI getInstance() {
        if(sAPI == null) {
            sAPI = new WeiboAPI();
        }
        return sAPI;
    }

    private static final String API_URL = "https://api.github.com";

    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        List<Post> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo
        );
    }

    public List<Post> getContributors() {
        return gitHub.contributors("square", "retrofit");

    }


}
