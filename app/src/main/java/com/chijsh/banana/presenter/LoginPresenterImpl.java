package com.chijsh.banana.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.chijsh.banana.Config;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by chijsh on 12/18/14.
 */
public class LoginPresenterImpl implements LoginPresenter, WeiboAuthListener {

    private LoginView mLoginView;
    private SsoHandler mSsoHandler;
    private WeiboAuth mWeiboAuth;

    public LoginPresenterImpl(LoginView loginView) {
        mLoginView = loginView;
    }

    @Override
    public void auth(Activity activity) {
        mWeiboAuth = new WeiboAuth(activity, Config.APP_KEY, Config.REDIRECT_URL, Config.SCOPE);
        mSsoHandler = new SsoHandler(activity, mWeiboAuth);
        mSsoHandler.authorize(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onComplete(Bundle bundle) {
        mLoginView.loginCompleted(bundle);
    }

    @Override
    public void onWeiboException(WeiboException e) {
        mLoginView.loginException(e);
    }

    @Override
    public void onCancel() {
        mLoginView.loginCancelled();
    }
}
