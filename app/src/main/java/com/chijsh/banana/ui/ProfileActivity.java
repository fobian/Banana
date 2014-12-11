package com.chijsh.banana.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.chijsh.banana.R;
import com.chijsh.banana.ui.post.PostActivity;
import com.chijsh.banana.utils.Utility;
import com.chijsh.banana.widget.BezelImageView;
import com.chijsh.banana.widget.tab.SlidingTabLayout;
import com.enrique.stackblur.StackBlurManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>
    {

    @InjectView(R.id.profile_avatar) BezelImageView mAvatar;
    @InjectView(R.id.profile_name) TextView mName;
    @InjectView(R.id.profile_description) TextView mDescription;
    @InjectView(R.id.avatar_bg) View mAvatarBg;
    @InjectView(R.id.profile_tabs) SlidingTabLayout mSlidingTab;
    @InjectView(R.id.view_pager) ViewPager mViewPager;
    //@InjectView(R.id.swipe_refresh_layout) View mSwipeRefreshLayout;

    //@InjectView(R.id.scroll_view) ObservableScrollView mScrollView;
    @InjectView(R.id.header) View mHeader;
    //@InjectView(R.id.headerbar) View mHeaderBar;

    @InjectView(R.id.toolbar_actionbar) Toolbar mToolbar;
    @InjectView(R.id.my_avatar) ImageView mToolBarAvatar;
    @InjectView(R.id.my_name) TextView mToolBarName;

    MyPagerAdapter mPagerAdapter;

    StackBlurManager mBlurManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.inject(this);

        mPagerAdapter = new MyPagerAdapter(getFragmentManager());
        //mPagerAdapter.setTabHolderScrollingContent(this);
        mViewPager.setAdapter(mPagerAdapter);
        //mViewPager.setOnPageChangeListener(this);

        // it's PagerAdapter set.
        mSlidingTab.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

        Resources res = getResources();
        mSlidingTab.setSelectedIndicatorColors(res.getColor(R.color.tab_selected_strip));
        mSlidingTab.setDistributeEvenly(true);
        mSlidingTab.setViewPager(mViewPager);
        //mSlidingTab.setOnPageChangeListener(this);

//        mSwipeRefreshLayout.setColorSchemeColors(
//                getResources().getColor(R.color.refresh_progress_1),
//                getResources().getColor(R.color.refresh_progress_2),
//                getResources().getColor(R.color.refresh_progress_3)
//        );

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra(PostActivity.NAME_EXTRA);
//            if (avatar != null) {
//                mAvatar.setImageBitmap(avatar);
//                mBlurManager = new StackBlurManager(avatar);
//                if (Utility.getSDKVersion() >= 16) {
//                    mAvatarBg.setBackground(new BitmapDrawable(getResources(), mBlurManager.process(20)));
//                } else {
//                    mAvatarBg.setBackgroundDrawable(new BitmapDrawable(getResources(), mBlurManager.process(20)));
//                }
//            }
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
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    WeiboFragment weiboFragment = WeiboFragment.newInstance("", "");
                    return weiboFragment;
                case 1:
                    FollowsFragment followsFragment = FollowsFragment.newInstance(true);
                    return followsFragment;
                case 2:
                    FollowsFragment followersFragment = FollowsFragment.newInstance(false);
                    return followersFragment;
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

}
