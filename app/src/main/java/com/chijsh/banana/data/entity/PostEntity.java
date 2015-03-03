package com.chijsh.banana.data.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by chijsh on 3/2/15.
 */
public class PostEntity extends RealmObject {

    @PrimaryKey
    public String idstr;
    public String createdAt;
    public String text;
    public String source;
    public boolean favorited;
    //public boolean truncated;
    public RealmList<PicUrlEntity> picUrls;
    public GeoEntity geo;
    public UserEntity user;
    public PostEntity retweetedStatus;
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

    public RealmList<PicUrlEntity> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(RealmList<PicUrlEntity> picUrls) {
        this.picUrls = picUrls;
    }

    public GeoEntity getGeo() {
        return geo;
    }

    public void setGeo(GeoEntity geo) {
        this.geo = geo;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public PostEntity getRetweetedStatus() {
        return retweetedStatus;
    }

    public void setRetweetedStatus(PostEntity retweetedStatus) {
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
