package com.chijsh.banana.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.Config;
import com.chijsh.banana.R;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends ActionBarActivity implements WeiboAuthListener {

    @InjectView(R.id.login)
    Button loginButton;

    private WeiboAuth mWeiboAuth;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        mWeiboAuth = new WeiboAuth(this, Config.APP_KEY, Config.REDIRECT_URL, Config.SCOPE);

        if(AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @OnClick(R.id.login)
    public void login() {
        mSsoHandler = new SsoHandler(this, mWeiboAuth);
        mSsoHandler.authorize(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //int id = item.getItemId();
        //if (id == R.id.action_settings) {
        //    return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onComplete(Bundle values) {
        mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        if (mAccessToken.isSessionValid()) {

            // 保存 Token 到 SharedPreferences
            AccessTokenKeeper.writeAccessToken(this, mAccessToken);
            Toast.makeText(this, R.string.weibo_auth_success, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            // 以下几种情况，您会收到 Code：
            // 1. 当您未在平台上注册的应用程序的包名与签名时；
            // 2. 当您注册的应用程序包名与签名不正确时；
            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            String code = values.getString("code");
            String message = getString(R.string.weibo_auth_failed);
            if (!TextUtils.isEmpty(code)) {
                message = message + "\nObtained the code: " + code;
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        Toast.makeText(this,
                "Auth exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, R.string.weibo_auth_cancel, Toast.LENGTH_SHORT).show();
    }
}
