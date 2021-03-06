package com.chijsh.banana.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.presentation.view.fragment.TimeLineFragment;
import com.chijsh.banana.sync.WeiboSyncAdapter;

public class TimeLineActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timeline);

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

}
