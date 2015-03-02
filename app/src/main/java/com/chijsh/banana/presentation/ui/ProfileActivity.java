package com.chijsh.banana.presentation.ui;

import android.animation.TimeInterpolator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.data.PostContract.AccountEntry;
import com.chijsh.banana.data.PostContract.UserEntry;
import com.chijsh.banana.presentation.ui.post.PostActivity;
import com.chijsh.banana.presentation.widget.BezelImageView;
import com.chijsh.banana.presentation.widget.tab.SlidingTabLayout;
import com.enrique.stackblur.StackBlurManager;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProfileActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private static final int PROFILE_LOADER = 0;

    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final int ANIM_DURATION = 1000;
    int mLeftDelta;
    int mTopDelta;
    float mWidthScale;
    float mHeightScale;


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

    private MyPagerAdapter mPagerAdapter;

    private StackBlurManager mBlurManager;

    private String mUserId;

    private static final String[] MY_PROFILE_COLUMNS = {
            AccountEntry.TABLE_NAME + "." + AccountEntry._ID,
            AccountEntry.COLUMN_USER_ID,
            AccountEntry.COLUMN_SCREEN_NAME,
//            AccountEntry.COLUMN_PROVINCE,
//            AccountEntry.COLUMN_CITY,
//            AccountEntry.COLUMN_LOCATION,
            AccountEntry.COLUMN_DESCRIPTION,
//            AccountEntry.COLUMN_PROFILE_URL,
//            AccountEntry.COLUMN_GENDER,
//            AccountEntry.COLUMN_FOLLOWERS_COUNT,
//            AccountEntry.COLUMN_FRIENDS_COUNT,
//            AccountEntry.COLUMN_STATUSES_COUNT,
//            AccountEntry.COLUMN_FAVOURITES_COUNT,
//            AccountEntry.COLUMN_CREATED_AT,
//            AccountEntry.COLUMN_FOLLOWING,
            AccountEntry.COLUMN_AVATAR_LARGE,
//              AccountEntry.COLUMN_FOLLOW_ME,

    };

    private static final String[] USER_PROFILE_COLUMNS = {
            UserEntry.TABLE_NAME + "." + UserEntry._ID,
            UserEntry.COLUMN_USER_ID,
            UserEntry.COLUMN_SCREEN_NAME,
//            UserEntry.COLUMN_PROVINCE,
//            UserEntry.COLUMN_CITY,
//            UserEntry.COLUMN_LOCATION,
            UserEntry.COLUMN_DESCRIPTION,
//            UserEntry.COLUMN_PROFILE_URL,
//            UserEntry.COLUMN_GENDER,
//            UserEntry.COLUMN_FOLLOWERS_COUNT,
//            UserEntry.COLUMN_FRIENDS_COUNT,
//            UserEntry.COLUMN_STATUSES_COUNT,
//            UserEntry.COLUMN_FAVOURITES_COUNT,
//            UserEntry.COLUMN_CREATED_AT,
//            UserEntry.COLUMN_FOLLOWING,
            UserEntry.COLUMN_AVATAR_LARGE,
//              UserEntry.COLUMN_FOLLOW_ME,

    };



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

        Bundle bundle = getIntent().getExtras();
        mUserId = bundle.getString(PostActivity.USER_ID_EXTRA);

        final int thumbnailTop = bundle.getInt(PostActivity.EXTRA_AVATAR_TOP);
        final int thumbnailLeft = bundle.getInt(PostActivity.EXTRA_AVATAR_LEFT);
        final int thumbnailWidth = bundle.getInt(PostActivity.EXTRA_AVATAR_WIDTH);
        final int thumbnailHeight = bundle.getInt(PostActivity.EXTRA_AVATAR_HEIGHT);

        if (savedInstanceState == null) {

            ViewTreeObserver observer = mAvatar.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    mAvatar.getViewTreeObserver().removeOnPreDrawListener(this);

                    // Figure out where the thumbnail and full size versions are, relative
                    // to the screen and each other
                    int[] screenLocation = new int[2];
                    mAvatar.getLocationOnScreen(screenLocation);
                    mLeftDelta = thumbnailLeft - screenLocation[0];
                    mTopDelta = thumbnailTop - screenLocation[1];

                    // Scale factors to make the large version the same size as the thumbnail
                    mWidthScale = (float) thumbnailWidth / mAvatar.getWidth();
                    mHeightScale = (float) thumbnailHeight / mAvatar.getHeight();

                    runEnterAnimation(mAvatar);

                    return true;
                }
            });
        }

        getLoaderManager().initLoader(PROFILE_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri uri;
        String[] projection;
        String selection;
        if (mUserId.equals(AccessTokenKeeper.readAccessToken(this).getUid())) {
            uri = AccountEntry.CONTENT_URI;
            projection = MY_PROFILE_COLUMNS;
            selection = AccountEntry.COLUMN_USER_ID + " = '" + mUserId + "'";
        } else {
            uri = UserEntry.CONTENT_URI;
            projection = USER_PROFILE_COLUMNS;
            selection = UserEntry.COLUMN_USER_ID + " = '" + mUserId + "'";
        }

        return new CursorLoader(
                this,
                uri,
                projection,
                selection,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor != null && cursor.moveToFirst()) {

            String avatarUrl;
            String name;
            String description;

            if (mUserId.equals(AccessTokenKeeper.readAccessToken(this).getUid())) {
                avatarUrl = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_AVATAR_LARGE));
                name = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_SCREEN_NAME));
                description = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_DESCRIPTION));
            } else {
                avatarUrl = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_AVATAR_LARGE));
                name = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_SCREEN_NAME));
                description = cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_DESCRIPTION));
            }
            Glide.with(this)
                    .load(avatarUrl)
                    .asBitmap()
                    .placeholder(R.drawable.user_avatar_empty)
                    .into(new SimpleTarget<Bitmap>(128, 128) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            mAvatar.setImageBitmap(resource);

                            Palette.generateAsync(resource, new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    int color = palette.getVibrantColor(getResources().getColor(R.color.tab_selected_strip));
                                    //mName.setTextColor(color);
                                    //mDescription.setTextColor(color);
                                    mSlidingTab.setSelectedIndicatorColors(color);
                                }
                            });

                            mBlurManager = new StackBlurManager(resource);
                            mAvatarBg.setBackground(new BitmapDrawable(getResources(), mBlurManager.process(20)));

                        }
                    });
            mName.setText(name);
            mDescription.setText(description);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void runEnterAnimation(View view) {
        final long duration = ANIM_DURATION;

        view.setPivotX(0);
        view.setPivotY(0);
        view.setScaleX(mWidthScale);
        view.setScaleY(mHeightScale);
        view.setTranslationX(mLeftDelta);
        view.setTranslationY(mTopDelta);

        view.animate().setDuration(duration).
                scaleX(1).scaleY(1).
                translationX(0).translationY(0).
                setInterpolator(sDecelerator);

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
                    FollowsFragment followsFragment = FollowsFragment.newInstance(mUserId, true);
                    return followsFragment;
                case 2:
                    FollowsFragment followersFragment = FollowsFragment.newInstance(mUserId, false);
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
