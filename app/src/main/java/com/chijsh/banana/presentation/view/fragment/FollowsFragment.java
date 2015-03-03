package com.chijsh.banana.presentation.view.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.manager.User;
import com.chijsh.banana.presentation.model.FollowsModel;
import com.chijsh.banana.data.net.WeiboAPI;
import com.chijsh.banana.presentation.view.adapter.FollowsAdapter;
import com.chijsh.banana.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FollowsFragment extends BaseFragment {


    @InjectView(R.id.list_follows) RecyclerView mFollowsRecyclerView;

    private FollowsAdapter mFollowsAdapter;
    private LinearLayoutManager mLayoutManager;

    private static final String ARG_IS_FOLLOWS = "args_is_follows";
    private static final String ARG_USER_ID = "args_user_id";

    private static final int COUNT_SINGLE_PAGE = 20;

    private String mUserId;
    private boolean mIsFollows;

    private int mNextCursor = 0;
    private boolean mIsLoading = false;
    private int mTotalNum = -1;

    public static FollowsFragment newInstance(String userId, boolean isFollows) {
        FollowsFragment fragment = new FollowsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        args.putBoolean(ARG_IS_FOLLOWS, isFollows);
        fragment.setArguments(args);
        return fragment;
    }

    public FollowsFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString(ARG_USER_ID);
            mIsFollows = getArguments().getBoolean(ARG_IS_FOLLOWS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_follows, container, false);
        ButterKnife.inject(this, rootView);
        mFollowsAdapter = new FollowsAdapter(getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        mFollowsRecyclerView.setLayoutManager(mLayoutManager);
        mFollowsRecyclerView.setAdapter(mFollowsAdapter);
        mFollowsRecyclerView.setHasFixedSize(true);
        mFollowsRecyclerView.setOnScrollListener(new ScrollToEndListener());
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadFollows();
    }

    @Override
    void initPresenter() {

    }

    private void loadFollows() {
        if (mTotalNum == mNextCursor) {
            Utility.toast(getActivity(), mIsFollows ? R.string.no_more_follows : R.string.no_more_followers);
            return;
        }
        User user = new User(WeiboAPI.getInstance(), null);
        String token = AccessTokenKeeper.readAccessToken(getActivity()).getToken();
        if (mIsFollows) {
            user.getFollows(token, Long.parseLong(mUserId), COUNT_SINGLE_PAGE, mNextCursor, new Callback<FollowsModel>() {
                @Override
                public void success(FollowsModel followsModel, Response response) {
                    mFollowsAdapter.addFollows(followsModel.users);
                    mNextCursor = followsModel.nextCursor;
                    mIsLoading = false;
                    mTotalNum = followsModel.totalNumber;

                }

                @Override
                public void failure(RetrofitError error) {
                    Utility.toast(getActivity(), error.getMessage());
                    mIsLoading = false;
                }
            });
        } else {
            user.getFollowers(token, Long.parseLong(mUserId), COUNT_SINGLE_PAGE, mNextCursor, new Callback<FollowsModel>() {
                @Override
                public void success(FollowsModel followsModel, Response response) {
                    mFollowsAdapter.addFollows(followsModel.users);
                    mNextCursor = followsModel.nextCursor;
                    mIsLoading = false;
                    mTotalNum = followsModel.totalNumber;
                    Log.d("sdsfsdfsdfsdfsdfsdfsdf", mNextCursor + "");
                }

                @Override
                public void failure(RetrofitError error) {
                    Utility.toast(getActivity(), error.getMessage());
                    mIsLoading = false;
                }
            });
        }
    }

    private class ScrollToEndListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition() + 1;
            int modelsCount = mFollowsAdapter.getItemCount();

            if (lastVisibleItemPosition == modelsCount) {
                if (!mIsLoading) {
                    mIsLoading = true;
                    loadFollows();
                }

            }
        }
    }
}
