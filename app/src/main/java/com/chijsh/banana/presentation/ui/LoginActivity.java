package com.chijsh.banana.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.presentation.presenter.LoginPresenter;
import com.chijsh.banana.presentation.presenter.LoginPresenterImpl;
import com.chijsh.banana.presentation.view.LoginView;
import com.chijsh.banana.utils.Utility;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {

    @InjectView(R.id.login)
    TextView mLoginButton;

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

        mPresenter.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void loginCompleted(Bundle bundle) {
        Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);
        if (accessToken.isSessionValid()) {
            AccessTokenKeeper.writeAccessToken(this, accessToken);
            Toast.makeText(this, R.string.weibo_auth_success, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, TimeLineActivity.class));
            finish();
        } else {
            String code = bundle.getString("code");
            String message = getString(R.string.weibo_auth_failed);
            if (!TextUtils.isEmpty(code)) {
                message = message + "\nObtained the code: " + code;
            }
            Utility.toast(this, message);
        }
    }

    @Override
    public void loginException(WeiboException e) {
        Utility.toast(this, "Auth exception : " + e.getMessage());
    }

    @Override
    public void loginCancelled() {
        Utility.toast(this, R.string.weibo_auth_cancel);
    }
}
