package com.chijsh.banana.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.Config;
import com.chijsh.banana.R;
import com.chijsh.banana.presenter.LoginPresenter;
import com.chijsh.banana.presenter.LoginPresenterImpl;
import com.chijsh.banana.presenter.LoginView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {

    @InjectView(R.id.login)
    TextView mLoginButton;

    private Oauth2AccessToken mAccessToken;
    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        mPresenter = new LoginPresenterImpl(this);

    }

    @OnClick(R.id.login)
    public void login() {
        mPresenter.auth(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        mPresenter.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void loginCompleted(Bundle bundle) {
        mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
        if (mAccessToken.isSessionValid()) {

            // 保存 Token 到 SharedPreferences
            AccessTokenKeeper.writeAccessToken(this, mAccessToken);
            Toast.makeText(this, R.string.weibo_auth_success, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, TimeLineActivity.class));
            finish();
        } else {
            // 以下几种情况，您会收到 Code：
            // 1. 当您未在平台上注册的应用程序的包名与签名时；
            // 2. 当您注册的应用程序包名与签名不正确时；
            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            String code = bundle.getString("code");
            String message = getString(R.string.weibo_auth_failed);
            if (!TextUtils.isEmpty(code)) {
                message = message + "\nObtained the code: " + code;
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void loginException(WeiboException e) {
        Toast.makeText(this,
                "Auth exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginCancelled() {
        Toast.makeText(this, R.string.weibo_auth_cancel, Toast.LENGTH_SHORT).show();
    }
}
