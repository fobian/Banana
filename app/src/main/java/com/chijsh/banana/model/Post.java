package com.chijsh.banana.model;

import java.util.List;

/**
 * Created by chijsh on 10/13/14.
 */

public class Post {
    public String createdAt;
    public String idstr;
    public String text;
    public String source;
    public int sourceType;
    public boolean favorited;
    public boolean truncated;
    public List<String> picUrls;
    public String geo;
    public User user;
}
