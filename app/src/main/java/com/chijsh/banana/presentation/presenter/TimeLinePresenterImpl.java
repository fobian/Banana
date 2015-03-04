package com.chijsh.banana.presentation.presenter;

import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.domain.Post;
import com.chijsh.banana.domain.interactor.TimeLineUseCase;
import com.chijsh.banana.presentation.model.mapper.PostModelDataMapper;
import com.chijsh.banana.presentation.view.TimeLineView;

import java.util.List;

/**
 * Created by chijsh on 3/2/15.
 */
public class TimeLinePresenterImpl implements TimeLinePresenter {

    private TimeLineView mTimeLineView;
    private TimeLineUseCase mTimeLineUseCase;

    public TimeLinePresenterImpl(TimeLineView mTimeLineView, TimeLineUseCase timeLineUseCase) {
        this.mTimeLineView = mTimeLineView;
        this.mTimeLineUseCase = timeLineUseCase;
    }

    @Override
    public void loadTimeLine() {
        mTimeLineView.showLoading();
        mTimeLineUseCase.execute(AccessTokenKeeper.readAccessToken(mTimeLineView.getContext()).getUid(), callback);
    }

    @Override
    public void nextPage() {

    }

    TimeLineUseCase.Callback callback = new TimeLineUseCase.Callback() {
        @Override
        public void onTimeLineLoaded(List<Post> posts) {
            mTimeLineView.hideLoading();
            mTimeLineView.renderTimeLine(PostModelDataMapper.transform(posts));
        }

        @Override
        public void onError(String error) {
            mTimeLineView.hideLoading();
            mTimeLineView.showError(error);
        }
    };
}
