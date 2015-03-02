package com.chijsh.banana.presentation.presenter;

import com.chijsh.banana.presentation.view.TimeLineView;

/**
 * Created by chijsh on 3/2/15.
 */
public class TimeLinePresenterImpl implements TimeLinePresenter {

    private TimeLineView mTimeLineView;

    public TimeLinePresenterImpl(TimeLineView mTimeLineView) {
        this.mTimeLineView = mTimeLineView;
    }

    @Override
    public void loadTimeLine() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void addFavourite(String postId) {

    }

    @Override
    public void removeFavourite(String postId) {

    }
}
