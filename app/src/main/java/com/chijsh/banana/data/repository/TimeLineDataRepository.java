package com.chijsh.banana.data.repository;

import com.chijsh.banana.data.entity.PostsEntity;
import com.chijsh.banana.data.entity.mapper.PostEntityDataMapper;
import com.chijsh.banana.data.repository.datasource.TimeLineDataStore;
import com.chijsh.banana.data.repository.datasource.TimeLineDataStoreFactory;
import com.chijsh.banana.domain.repository.TimeLineRepository;

/**
 * Created by chijsh on 3/2/15.
 */
public class TimeLineDataRepository implements TimeLineRepository {

    private static TimeLineDataRepository INSTANCE;
    private TimeLineDataStoreFactory mTimeLineFactory;

    public static synchronized TimeLineDataRepository getInstance(TimeLineDataStoreFactory dataStoreFactory) {
        if (INSTANCE == null) {
            INSTANCE = new TimeLineDataRepository(dataStoreFactory);
        }
        return INSTANCE;
    }


    protected TimeLineDataRepository(TimeLineDataStoreFactory dataStoreFactory) {
        this.mTimeLineFactory = dataStoreFactory;
    }

    @Override
    public void getUserTimeLine(long sinceId, final TimeLineCallback callback) {
        TimeLineDataStore dataStore = mTimeLineFactory.createCloudDataStore();
        dataStore.getTimeLine(sinceId, new TimeLineDataStore.TimeLineCallback() {
            @Override
            public void onTimeLineLoaded(PostsEntity postEntityList) {
                callback.onTimeLineLoaded(PostEntityDataMapper.transform(postEntityList.statuses));
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    @Override
    public void nextPage() {

    }
}
