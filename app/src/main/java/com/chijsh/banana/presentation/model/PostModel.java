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
    public UserModel user;
    public PostModel retweetedStatus;
    public int repostsCount;
    public int commentsCount;
    public int attitudesCount;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public List<PicUrlModel> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<PicUrlModel> picUrls) {
        this.picUrls = picUrls;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public PostModel getRetweetedStatus() {
        return retweetedStatus;
    }

    public void setRetweetedStatus(PostModel retweetedStatus) {
        this.retweetedStatus = retweetedStatus;
    }

    public int getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(int repostsCount) {
        this.repostsCount = repostsCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getAttitudesCount() {
        return attitudesCount;
    }

    public void setAttitudesCount(int attitudesCount) {
        this.attitudesCount = attitudesCount;
    }

    public boolean isMultiPics() {
        if(picUrls.size() > 1) {
            return true;
        }
        return false;
    }

}
