package com.chijsh.banana.presentation.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import com.chijsh.banana.R;

import com.chijsh.banana.data.cache.PostCache;
import com.chijsh.banana.data.cache.PostCacheImpl;
import com.chijsh.banana.data.cache.UserCache;
import com.chijsh.banana.data.cache.UserCacheImpl;
import com.chijsh.banana.data.repository.TimeLineDataRepository;
import com.chijsh.banana.data.repository.datasource.TimeLineDataStoreFactory;
import com.chijsh.banana.domain.interactor.TimeLineUseCase;
import com.chijsh.banana.domain.interactor.TimeLineUseCaseImpl;
import com.chijsh.banana.domain.repository.TimeLineRepository;
import com.chijsh.banana.presentation.model.PicUrlModel;
import com.chijsh.banana.presentation.model.PostModel;
import com.chijsh.banana.presentation.presenter.TimeLinePresenter;
import com.chijsh.banana.presentation.presenter.TimeLinePresenterImpl;
import com.chijsh.banana.presentation.view.TimeLineView;
import com.chijsh.banana.presentation.view.activity.PhotoViewActivity;
import com.chijsh.banana.presentation.view.activity.PostContentActivity;
import com.chijsh.banana.presentation.view.activity.ProfileActivity;
import com.chijsh.banana.presentation.view.activity.SettingsActivity;
import com.chijsh.banana.presentation.view.adapter.TimeLineAdapter;
import com.chijsh.banana.presentation.view.widget.DividerItemDecoration;
import com.chijsh.banana.presentation.view.activity.PostActivity;
import com.chijsh.banana.utils.Utility;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by chijsh on 10/20/14.
 */
public class TimeLineFragment extends BaseFragment implements
        TimeLineAdapter.PostItemClickListener,
        SwipeRefreshLayout.OnRefreshListener ,
        TimeLineView {
//    private static final int POST_LOADER = 0;

    public static final String POST_ID = "post_id";

    private TimeLineAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TimeLinePresenter mTimeLinePresenter;

    @InjectView(R.id.swipe_refresh) SwipeRefreshLayout mSwipeLayout;
    @InjectView(R.id.time_line) RecyclerView mRecyclerView;
    @InjectView(R.id.fab) ImageButton mFloatingButton;

//    private static final String[] POST_COLUMNS = {
//            PostEntry.TABLE_NAME + "." + PostEntry._ID,
//            PostEntry.COLUMN_CREATED_AT,
//            PostEntry.COLUMN_POST_ID,
//            PostEntry.COLUMN_POST_TEXT,
//            PostEntry.COLUMN_POST_SOURCE,
//            PostEntry.COLUMN_POST_FAVORITED,
//            PostEntry.COLUMN_POST_PICURLS,
//            PostEntry.COLUMN_POST_GEO,
//            PostEntry.COLUMN_USER_ID,
//            PostEntry.COLUMN_USER_SCREENNAME,
//            PostEntry.COLUMN_USER_AVATAR,
//            PostEntry.COLUMN_RETWEETED_ID,
//            PostEntry.COLUMN_RETWEETED_USER_SCREENNAME,
//            PostEntry.COLUMN_RETWEETED_TEXT,
//            PostEntry.COLUMN_RETWEETED_PICURLS,
//            PostEntry.COLUMN_REPOST_COUNT,
//            PostEntry.COLUMN_COMMENT_COUNT,
//            PostEntry.COLUMN_ATTITUDE_COUNT,
//
//    };

    private static final int ANIM_DURATION_FAB = 400;

    public TimeLineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_timeline, container, false);

        ButterKnife.inject(this, rootView);

        mSwipeLayout.setColorSchemeColors(
                getResources().getColor(R.color.refresh_progress_1),
                getResources().getColor(R.color.refresh_progress_2),
                getResources().getColor(R.color.refresh_progress_3));

        mSwipeLayout.setOnRefreshListener(this);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TimeLineAdapter(getActivity(), null);
        mAdapter.setPostItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDimensionPixelSize(R.dimen.divider_padding)));

        mFloatingButton.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.fab_size));

        return rootView;
    }

    @Override
    void initPresenter() {
        Realm realm = Realm.getInstance(getActivity());
        PostCache postCache = PostCacheImpl.getInstance(realm);
        UserCache userCache = UserCacheImpl.getInstance(realm);
        TimeLineDataStoreFactory dataStoreFactory = new TimeLineDataStoreFactory(getActivity(), postCache, userCache);
        TimeLineRepository timeLineRepository = TimeLineDataRepository.getInstance(dataStoreFactory);
        TimeLineUseCase timeLineUseCase = new TimeLineUseCaseImpl(timeLineRepository);
        mTimeLinePresenter = new TimeLinePresenterImpl(this, timeLineUseCase);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {
        Utility.toast(getActivity(), message);
    }

    @Override
    public void viewPost(PostModel post) {

    }

    @Override
    public void renderTimeLine(List<PostModel> posts) {
        mAdapter.setData(posts);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    private void startFABAnimation() {

        mFloatingButton.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();
    }

    @Override
    public void onRefresh() {
//        PrefUtil.markManuallySync(getActivity(), true);
//        refreshTimeLine();
        mTimeLinePresenter.loadTimeLine();
    }

    @Override
    public void onItemClicked(View itemView, String postId) {
        Intent intent = new Intent(getActivity(), PostContentActivity.class);
        intent.putExtra(POST_ID, postId);
        startActivity(intent);
    }

    @Override
    public void onAvatarClicked(String userId) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(PostActivity.USER_ID_EXTRA, userId);
        startActivity(intent);
    }

    @Override
    public void onFavouriteActionClicked(String postId, boolean isFavourited) {
//        Weibor weibor = new Weibor(WeiboAPI.getInstance());
//        String token = AccessTokenKeeper.readAccessToken(getActivity()).getToken();
//        if (isFavourited) {
//            weibor.deleteFavorites(token, Long.valueOf(postId), new Callback<Response>() {
//                @Override
//                public void success(Response response, Response response2) {
//                    Utility.toast(getActivity(), R.string.deleted_favourite);
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Utility.toast(getActivity(), R.string.delete_favourite_failure);
//                }
//            });
//        } else {
//            weibor.addFavorites(token, Long.valueOf(postId), new Callback<Response>() {
//                @Override
//                public void success(Response response, Response response2) {
//                    Utility.toast(getActivity(), R.string.added_to_favourite);
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Utility.toast(getActivity(), R.string.added_favourite_failure);
//                }
//            });
//        }
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
    public void onImageClicked(View view, int position, List<PicUrlModel> pics) {
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
        intent.putExtra(PhotoViewActivity.EXTRA_PHOTO_LEFT, screenLocation[0]);
        intent.putExtra(PhotoViewActivity.EXTRA_PHOTO_TOP, screenLocation[1]);
        intent.putExtra(PhotoViewActivity.EXTRA_PHOTO_WIDTH, view.getWidth());
        intent.putExtra(PhotoViewActivity.EXTRA_PHOTO_HEIGHT, view.getHeight());

        intent.putExtra(PhotoViewActivity.EXTRA_PHOTO_POSITION, position);
        //intent.putExtra(PhotoViewActivity.EXTRA_PHOTO_ARRAY, getOriginalPics(pics));
        getActivity().overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private String[] getOriginalPics(String[] pics) {
        int length = pics.length;
        String[] newPics = new String[length];
        String newString;
        for (int i = 0; i < length; ++i) {
            newString = pics[i].replace("thumbnail", "large");
            newPics[i] = newString;
        }
        return newPics;
    }

//    @Override
//    public void onRefresh() {
//        PrefUtil.markManuallySync(getActivity(), true);
//        refreshTimeLine();
//    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }

//    private void refreshTimeLine() {
//        WeiboSyncAdapter.triggerSyncAdapter(getActivity());
//    }

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
        mTimeLinePresenter.loadTimeLine();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.time_line, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.action_me:
                return true;
            case R.id.action_search:
                //startActivity(new Intent(this, NotificationActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
