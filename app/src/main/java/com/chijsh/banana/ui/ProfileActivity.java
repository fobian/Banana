package com.chijsh.banana.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chijsh.banana.R;
import com.chijsh.banana.ui.post.PostActivity;
import com.chijsh.banana.widget.BezelImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileActivity extends BaseActivity {

    @InjectView(R.id.profile_avatar) BezelImageView mAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.inject(this);
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle(R.string.title_activity_profile);
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        final Bitmap bitmap = intent.getParcelableExtra(PostActivity.MY_AVATAR);
        if (bitmap != null) {
            mAvatar.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
