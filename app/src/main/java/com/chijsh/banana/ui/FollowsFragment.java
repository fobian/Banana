package com.chijsh.banana.ui;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chijsh.banana.R;
import com.chijsh.banana.event.MessageEvent;
import com.chijsh.banana.event.UserEvent;
import com.chijsh.banana.service.FollowsService;
import com.chijsh.banana.utils.Utility;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;

public class FollowsFragment extends Fragment {


    @InjectView(R.id.list_follows) RecyclerView mFollowsRecyclerView;

    private FollowsAdapter mFollowsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public FollowsFragment() {
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
        mFollowsAdapter.setFollows(event.getUsers());
        mFollowsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().startService(new Intent(getActivity(), FollowsService.class));
    }
}
