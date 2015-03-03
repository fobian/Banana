package com.chijsh.banana.data.repository.datasource;

import com.chijsh.banana.data.entity.PostsEntity;

/**
 * Created by chijsh on 3/3/15.
 */
public interface TimeLineDataStore {

    interface TimeLineCallback {
        void onTimeLineLoaded(PostsEntity postEntityList);

        void onError(String errorMessage);
    }

    void getTimeLine(long sinceId, TimeLineCallback postEntityCallback);

    void nextPage();

}
