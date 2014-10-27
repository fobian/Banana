package com.chijsh.banana.model;

import java.util.List;

/**
 * Created by chijsh on 10/27/14.
 */
public class Posts {
    public List<Post> statuses;
    public int size() {
        return statuses.size();
    }
    public Post get(int i) {
        return statuses.get(i);
    }
}
