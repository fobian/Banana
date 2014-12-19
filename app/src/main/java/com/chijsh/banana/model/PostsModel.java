package com.chijsh.banana.model;

import java.util.List;

/**
 * Created by chijsh on 10/27/14.
 */
public class PostsModel {
    public List<PostModel> statuses;
    public int size() {
        return statuses.size();
    }
    public PostModel get(int i) {
        return statuses.get(i);
    }
}
