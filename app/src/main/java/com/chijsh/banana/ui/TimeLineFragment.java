package com.chijsh.banana.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chijsh.banana.R;

import com.chijsh.banana.data.PostContract.PostEntry;
import com.chijsh.banana.service.FavouriteWeiboService;
import com.chijsh.banana.sync.WeiboSyncAdapter;
import com.chijsh.banana.ui.post.PostActivity;
import com.chijsh.banana.utils.PrefUtil;
import com.chijsh.banana.utils.Utility;
import com.chijsh.banana.widget.MultiSwipeRefreshLayout;
import com.chijsh.banana.widget.fab.FloatingActionButton;
import com.chijsh.banana.widget.observablerecyclerview.ObservableRecyclerView;
import com.chijsh.banana.widget.observablerecyclerview.ObservableScrollViewCallbacks;
import com.chijsh.banana.widget.observablerecyclerview.ScrollState;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by chijsh on 10/20/14.
 */
public class TimeLineFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
        , SwipeRefreshLayout.OnRefreshListener
        , TimeLineAdapter.PostItemClickListener
        , ObservableScrollViewCallbacks
        , MultiSwipeRefreshLayout.CanChildScrollUpCallback {
    private static final int POST_LOADER = 0;

    public static final String POST_ID = "post_id";

    public static final String POST_ID_EXTRA = "post_id_extra";
    public static final String IS_FAVOURITED_EXTRA = "is_favourited_extra";

    private TimeLineAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @InjectView(R.id.swip_refresh_layout) MultiSwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.time_line) ObservableRecyclerView mRecyclerView;
    @InjectView(R.id.fab) FloatingActionButton mFloatingButton;

    private View mHeaderView;
    private int mBaseTranslationY;

    private static final String[] POST_COLUMNS = {
            PostEntry.TABLE_NAME + "." + PostEntry._ID,
            PostEntry.COLUMN_CREATED_AT,
            PostEntry.COLUMN_POST_ID,
            PostEntry.COLUMN_POST_TEXT,
            PostEntry.COLUMN_POST_SOURCE,
            PostEntry.COLUMN_POST_FAVORITED,
            PostEntry.COLUMN_POST_PICURLS,
            PostEntry.COLUMN_POST_GEO,
            PostEntry.COLUMN_USER_ID,
            PostEntry.COLUMN_USER_SCREENNAME,
            PostEntry.COLUMN_USER_AVATAR,
            PostEntry.COLUMN_RETWEETED_ID,
            PostEntry.COLUMN_RETWEETED_USER_SCREENNAME,
            PostEntry.COLUMN_RETWEETED_TEXT,
            PostEntry.COLUMN_RETWEETED_PICURLS,
            PostEntry.COLUMN_REPOST_COUNT,
            PostEntry.COLUMN_COMMENT_COUNT,
            PostEntry.COLUMN_ATTITUDE_COUNT,

    };

    public TimeLineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        ButterKnife.inject(this, rootView);

        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.refresh_progress_1),
                getResources().getColor(R.color.refresh_progress_2),
                getResources().getColor(R.color.refresh_progress_3)
                );
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setCanChildScrollUpCallback(this);

        mFloatingButton.attachToRecyclerView(mRecyclerView);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TimeLineAdapter(getActivity(), null);
        mAdapter.setPostItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setScrollViewCallbacks(this);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(16));

        mHeaderView = ((TimeLineActivity)getActivity()).getHeaderView();
        return rootView;
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        mBaseTranslationY = 0;

        float headerTranslationY = mHeaderView.getTranslationY();
        int toolbarHeight = mHeaderView.getHeight();
        if (scrollState == ScrollState.UP) {
            if (toolbarHeight < mRecyclerView.getCurrentScrollY()) {
                if (headerTranslationY != -toolbarHeight) {
                    mHeaderView.animate().cancel();
                    mHeaderView.animate().translationY(-toolbarHeight).setDuration(200).start();
                }
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarHeight < mRecyclerView.getCurrentScrollY()) {
                if (headerTranslationY != 0) {
                    mHeaderView.animate().cancel();
                    mHeaderView.animate().translationY(0).setDuration(200).start();
                }
            }
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = mHeaderView.getHeight();
            if (firstScroll) {
                float currentHeaderTranslationY = mHeaderView.getTranslationY();
                if (-toolbarHeight < currentHeaderTranslationY && toolbarHeight < scrollY) {
                    mBaseTranslationY = scrollY;
                }
            }
            int headerTranslationY = Math.min(0, Math.max(-toolbarHeight, -(scrollY - mBaseTranslationY)));
            mHeaderView.animate().cancel();
            mHeaderView.setTranslationY(headerTranslationY);
        }
    }

    @Override
    public void onItemClicked(View itemView, String postId) {
        Intent intent = new Intent(getActivity(), PostContentActivity.class);
        intent.putExtra(POST_ID, postId);
        startActivity(intent);
    }

    @Override
    public void onAvatarClicked(String userName) {
        Utility.toast(getActivity(), userName);
    }

    @Override
    public void onFavouriteActionClicked(String postId, boolean isFavourited) {
        Intent intent = new Intent(getActivity(), FavouriteWeiboService.class);
        intent.putExtra(POST_ID_EXTRA, postId);
        intent.putExtra(IS_FAVOURITED_EXTRA, isFavourited);
        getActivity().startService(intent);
    }

    @Override
    public void onCommentActionClicked(String postId) {
        Utility.toast(getActivity(), postId);
    }

    @Override
    public void onForwardActionClicked(String postId) {
        Utility.toast(getActivity(), postId);
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return canRecyclerViewScrollUp();
    }

    @Override
    public void onRefresh() {
        PrefUtil.markManuallySync(getActivity(), true);
        refreshTimeLine();
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }

    private void refreshTimeLine() {
        WeiboSyncAdapter.triggerSyncAdapter(getActivity());
    }

    @OnClick(R.id.fab)
    public void postNewWeibo() {
        startActivity(new Intent(getActivity(), PostActivity.class));
    }

    public boolean canRecyclerViewScrollUp() {
        return ViewCompat.canScrollVertically(mRecyclerView, -1);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(POST_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        Uri uri = PostEntry.CONTENT_URI;
        String sortOrder = PostEntry._ID + " DESC";

        return new CursorLoader(
                getActivity(),
                uri,
                POST_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


}
