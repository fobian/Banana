package com.chijsh.banana.domain.interactor;

import com.chijsh.banana.domain.Post;

import java.util.List;

/**
 * Created by chijsh on 3/2/15.
 */
public interface TimeLineUseCase extends Interactor {

    interface Callback {
        void onTimeLineLoaded(List<Post> posts);
        void onError(String error);
    }

    public void execute(String userId, Callback callback);
}
