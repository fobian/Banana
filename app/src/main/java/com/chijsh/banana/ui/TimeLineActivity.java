package com.chijsh.banana.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.View;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.sync.WeiboSyncAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TimeLineActivity extends BaseActivity {

    @InjectView(R.id.headerbar) View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timeline);

        ButterKnife.inject(this);

        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle(R.string.app_name);

        if(!AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new TimeLineFragment())
                    .commit();
        }

        WeiboSyncAdapter.initializeSyncAdapter(this);

    }

    public View getHeaderView() {
        return mHeaderView;
    }

}
