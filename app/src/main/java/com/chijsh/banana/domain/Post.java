package com.chijsh.banana.domain;

import java.util.List;

/**
 * Created by chijsh on 3/2/15.
 */
public class Post {

    public String idstr;
    public String createdAt;
    public String text;
    public String source;
    public boolean favorited;
    //public boolean truncated;
    public List<PicUrl> picUrls;
    public User user;
    public Post retweetedStatus;
    public int repostsCount;
    public int commentsCount;
    public int attitudesCount;

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public List<PicUrl> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<PicUrl> picUrls) {
        this.picUrls = picUrls;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getRetweetedStatus() {
        return retweetedStatus;
    }

    public void setRetweetedStatus(Post retweetedStatus) {
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
}
