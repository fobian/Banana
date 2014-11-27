package com.chijsh.banana.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
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

import com.chijsh.banana.R;
import com.chijsh.banana.ui.post.PostActivity;
import com.chijsh.banana.utils.Blur;
import com.chijsh.banana.widget.BezelImageView;
import com.chijsh.banana.widget.MultiSwipeRefreshLayout;
import com.chijsh.banana.widget.tab.SlidingTabLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileActivity extends BaseActivity {

    @InjectView(R.id.profile_avatar) BezelImageView mAvatar;
    @InjectView(R.id.avatar_bg) View mAvatarBg;
    @InjectView(R.id.profile_tabs) SlidingTabLayout mSlidingTab;
    @InjectView(R.id.view_pager) ViewPager mViewPager;
    @InjectView(R.id.swipe_refresh_layout) MultiSwipeRefreshLayout mSwipeRefreshLayout;
    MyPagerAdapter mPagerAdapter;


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
            return new PlaceholderFragment();
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
