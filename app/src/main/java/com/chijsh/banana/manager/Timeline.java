package com.chijsh.banana.manager;

import com.chijsh.banana.presentation.model.PostsModel;
import com.chijsh.banana.data.net.WeiboAPI;

/**
 * Created by chijsh on 12/18/14.
 */
public class Timeline {
    private WeiboAPI mWeiboAPI;
    private String mUserName;

    public Timeline(WeiboAPI weiboAPI, String userName) {
        this.mWeiboAPI = weiboAPI;
        this.mUserName = userName;
    }

    public PostsModel getTimeLine(String token, long sinceId) {
        return mWeiboAPI.getTimeline(token, sinceId);
    }
}
