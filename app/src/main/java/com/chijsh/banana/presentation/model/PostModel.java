package com.chijsh.banana.presentation.model;

import java.util.List;

/**
 * Created by chijsh on 10/13/14.
 */

public class PostModel {
    public String createdAt;
    public String idstr;
    public String text;
    public String source;
    public boolean favorited;
    //public boolean truncated;
    public List<PicUrlModel> picUrls;
    public GeoModel geo;
    public UserModel user;
    public PostModel retweetedStatus;
    public int repostsCount;
    public int commentsCount;
    public int attitudesCount;

    public boolean isMultiPics() {
        if(picUrls.size() > 1) {
            return true;
        }
        return false;
    }

}
