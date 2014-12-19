package com.chijsh.banana.ui;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.manager.User;
import com.chijsh.banana.model.FollowsModel;
import com.chijsh.banana.network.WeiboAPI;

import butterknife.ButterKnife;
import butterknife.InjectView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FollowsFragment extends Fragment {


    @InjectView(R.id.list_follows) RecyclerView mFollowsRecyclerView;

    private FollowsAdapter mFollowsAdapter;
    private LinearLayoutManager mLayoutManager;

    private static final String ARG_IS_FOLLOWS = "args_is_follows";
    private static final String ARG_USER_ID = "args_user_id";

    private String mUserId;
    private boolean mIsFollows;

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
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        User user = new User(WeiboAPI.getInstance(), null);
        String token = AccessTokenKeeper.readAccessToken(getActivity()).getToken();
        if (mIsFollows) {
            user.getFollows(token, Long.parseLong(mUserId), new Callback<FollowsModel>() {
                @Override
                public void success(FollowsModel followsModel, Response response) {
                    mFollowsAdapter.setFollows(followsModel.users);
                    mFollowsAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        } else {
            user.getFollowers(token, Long.parseLong(mUserId), new Callback<FollowsModel>() {
                @Override
                public void success(FollowsModel followsModel, Response response) {
                    mFollowsAdapter.setFollows(followsModel.users);
                    mFollowsAdapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

}
