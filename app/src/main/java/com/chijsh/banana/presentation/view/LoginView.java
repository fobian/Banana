package com.chijsh.banana.presentation.view;

import android.os.Bundle;

import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by chijsh on 12/18/14.
 */
public interface LoginView extends View {
    public void loginCompleted(Bundle bundle);
    public void loginException(WeiboException e);
    public void loginCancelled();
}
