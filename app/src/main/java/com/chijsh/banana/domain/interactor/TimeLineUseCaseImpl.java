package com.chijsh.banana.domain.interactor;

import com.chijsh.banana.domain.Post;
import com.chijsh.banana.domain.repository.TimeLineRepository;

import java.util.List;

/**
 * Created by chijsh on 3/2/15.
 */
public class TimeLineUseCaseImpl implements TimeLineUseCase {

    private TimeLineRepository mTimeLineRepo;

    private Callback mCallback;

    private String mUserId;

    public TimeLineUseCaseImpl(TimeLineRepository repository) {
        mTimeLineRepo = repository;
    }

    private TimeLineRepository.TimeLineCallback timeLineCallback = new TimeLineRepository.TimeLineCallback() {
        @Override
        public void onTimeLineLoaded(List<Post> posts) {
            mCallback.onTimeLineLoaded(posts);
        }

        @Override
        public void onError(String error) {
            mCallback.onError(error);
        }
    };

    @Override
    public void execute(String userId, Callback callback) {
        mUserId = userId;
        mCallback = callback;
        mTimeLineRepo.getUserTimeLine(mUserId, timeLineCallback);
    }

}
