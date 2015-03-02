package com.chijsh.banana.domain.interactor;

import com.chijsh.banana.domain.repository.TimeLineRepository;

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

    @Override
    public void execute(String userId, Callback callback) {
        mUserId = userId;
        mCallback = callback;

    }

    @Override
    public void run() {
        mTimeLineRepo.getUserTimeLine(mUserId);
    }
}
