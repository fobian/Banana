package com.chijsh.banana.service;

import android.app.IntentService;
import android.content.Intent;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.api.WeiboAPI;
import com.chijsh.banana.ui.PostActivity;

/**
 * Created by chijsh on 11/7/14.
 */
public class PostWeiboService extends IntentService {

    public PostWeiboService() {
        super("PostWeiboService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String content = intent.getStringExtra(PostActivity.POST_WEIBO_EXTRA);
        String token = AccessTokenKeeper.readAccessToken(this).getToken();
        WeiboAPI.getInstance().postWeibo(token, content);
    }
}
