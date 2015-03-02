package com.chijsh.banana.domain.repository;

import com.chijsh.banana.domain.Post;
import com.chijsh.banana.domain.exception.ErrorBundle;

import java.util.List;

/**
 * Created by chijsh on 3/2/15.
 */
public interface TimeLineRepository {

    interface TimeLineCallback {
        void onTimeLineLoaded(List<Post> posts);

        void onError(ErrorBundle errorBundle);
    }

    public void getUserTimeLine(String userId);

    public void nextPage();

}
