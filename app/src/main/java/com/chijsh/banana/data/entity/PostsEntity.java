package com.chijsh.banana.data.entity;

import java.util.List;

/**
 * Created by chijsh on 3/3/15.
 */
public class PostsEntity {
    public List<PostEntity> statuses;
    public int size() {
        return statuses.size();
    }
    public PostEntity get(int i) {
        return statuses.get(i);
    }
}
