package com.chijsh.banana.ui;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chijsh.banana.R;
import com.chijsh.banana.event.UserEvent;
import com.chijsh.banana.service.FollowsService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class FollowsFragment extends Fragment {


    @InjectView(R.id.list_follows) RecyclerView mFollowsRecyclerView;

    private FollowsAdapter mFollowsAdapter;
    private LinearLayoutManager mLayoutManager;

    private static final String ARG_IS_FOLLOWS = "args_is_follows";
    private static final String ARG_USER_ID = "args_user_id";
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_IS_FOLLOWS = "extra_is_follows";
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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(UserEvent event){
        if (event.isFollows() == mIsFollows) {
            mFollowsAdapter.setFollows(event.getUsers());
            mFollowsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = new Intent(getActivity(), FollowsService.class);
        intent.putExtra(EXTRA_USER_ID, mUserId);
        intent.putExtra(EXTRA_IS_FOLLOWS, mIsFollows);
        getActivity().startService(intent);
    }

}
