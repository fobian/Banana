package com.chijsh.banana.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chijsh.banana.R;
import com.chijsh.banana.ui.post.PostActivity;
import com.chijsh.banana.utils.Utility;
import com.chijsh.banana.widget.BezelImageView;
import com.chijsh.banana.widget.MultiSwipeRefreshLayout;
import com.chijsh.banana.widget.tab.SlidingTabLayout;
import com.enrique.stackblur.StackBlurManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.profile_avatar) BezelImageView mAvatar;
    @InjectView(R.id.profile_name) TextView mName;
    @InjectView(R.id.profile_description) TextView mDescription;
    @InjectView(R.id.avatar_bg) View mAvatarBg;
    @InjectView(R.id.profile_tabs) SlidingTabLayout mSlidingTab;
    @InjectView(R.id.view_pager) ViewPager mViewPager;
    @InjectView(R.id.swipe_refresh_layout) MultiSwipeRefreshLayout mSwipeRefreshLayout;
    MyPagerAdapter mPagerAdapter;

    StackBlurManager mBlurManager;


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

        mPagerAdapter = new MyPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        // it's PagerAdapter set.
        mSlidingTab.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

        Resources res = getResources();
        mSlidingTab.setSelectedIndicatorColors(res.getColor(R.color.tab_selected_strip));
        mSlidingTab.setDistributeEvenly(true);
        mSlidingTab.setViewPager(mViewPager);

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.refresh_progress_1),
                getResources().getColor(R.color.refresh_progress_2),
                getResources().getColor(R.color.refresh_progress_3)
        );

        Intent intent = getIntent();
        if (intent != null) {
            Bitmap avatar = intent.getParcelableExtra(PostActivity.AVATAR_EXTRA);
            String name = intent.getStringExtra(PostActivity.NAME_EXTRA);
            if (avatar != null) {
                mAvatar.setImageBitmap(avatar);
                mBlurManager = new StackBlurManager(avatar);
                if (Utility.getSDKVersion() >= 16) {
                    mAvatarBg.setBackground(new BitmapDrawable(getResources(), mBlurManager.process(20)));
                } else {
                    mAvatarBg.setBackgroundDrawable(new BitmapDrawable(getResources(), mBlurManager.process(20)));
                }
            }
            mName.setText(name);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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

    private class MyPagerAdapter extends FragmentPagerAdapter {

        private final int[] mTitles = {R.string.profile_weibo, R.string.profile_follow, R.string.profile_followers};

        private MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new PlaceholderFragment();
                case 1:
                    return new FollowsFragment();
                case 2:
                    return new PlaceholderFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(mTitles[position]);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
            return rootView;
        }
    }

}
