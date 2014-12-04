package com.chijsh.banana.ui;


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

public class FollowsFragment extends ScrollTabHolderFragment {


    @InjectView(R.id.list_follows) RecyclerView mFollowsRecyclerView;

    private FollowsAdapter mFollowsAdapter;
    private LinearLayoutManager mLayoutManager;

    private static final String ARG_IS_FOLLOWS = "is_follows";
    public static final String EXTRA_IS_FOLLOWS = "extra_is_follows";
    private boolean mIsFollows;

    public static FollowsFragment newInstance(boolean isFollows) {
        FollowsFragment fragment = new FollowsFragment();
        Bundle args = new Bundle();
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
        mFollowsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition() + 1;
                int totalItemCount = mLayoutManager.getItemCount();
                if (mScrollTabHolderListener != null)
                    mScrollTabHolderListener.onScroll(recyclerView, firstVisibleItem, visibleItemCount, totalItemCount, 0);

            }
        });
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
        intent.putExtra(EXTRA_IS_FOLLOWS, mIsFollows);
        getActivity().startService(intent);
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && mLayoutManager.findFirstVisibleItemPosition() >= 1) {
            return;
        }

        mLayoutManager.scrollToPositionWithOffset(1, scrollHeight);
    }
}
