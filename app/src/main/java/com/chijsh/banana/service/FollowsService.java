package com.chijsh.banana.service;

import android.app.IntentService;
import android.content.Intent;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.api.WeiboAPI;
import com.chijsh.banana.event.UserEvent;
import com.chijsh.banana.model.Follows;
import com.chijsh.banana.ui.FollowsFragment;

import de.greenrobot.event.EventBus;

/**
 * Created by chijsh on 11/7/14.
 */
public class FollowsService extends IntentService {

    public FollowsService() {
        super("FollowsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean isFollows = intent.getBooleanExtra(FollowsFragment.EXTRA_IS_FOLLOWS, true);
        String token = AccessTokenKeeper.readAccessToken(this).getToken();
        String uid = AccessTokenKeeper.readAccessToken(this).getUid();
        Follows users;
        if (isFollows) {
            users = WeiboAPI.getInstance().getFollows(token, Long.parseLong(uid));
        } else {
            users = WeiboAPI.getInstance().getFollowers(token, Long.parseLong(uid));
        }

        if (users != null) {
            EventBus.getDefault().post(new UserEvent(users.users, isFollows));
        }
    }
}
