package com.chijsh.banana.data.repository.datasource;

import com.chijsh.banana.data.cache.PostCache;

/**
 * Created by chijsh on 3/3/15.
 */
public class DiskTimeLineDataStore implements TimeLineDataStore {

    private PostCache mPostCache;

    public DiskTimeLineDataStore(PostCache mPostCache) {
        this.mPostCache = mPostCache;
    }

    @Override
    public void getTimeLine(long sinceId, TimeLineCallback postEntityCallback) {
        //TODO
    }

    @Override
    public void nextPage() {

    }

}
