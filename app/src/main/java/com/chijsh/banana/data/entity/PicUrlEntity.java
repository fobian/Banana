package com.chijsh.banana.data.entity;

import io.realm.RealmObject;

/**
 * Created by chijsh on 3/3/15.
 */
public class PicUrlEntity extends RealmObject {
    private String thumbnailPic;

    public String getThumbnailPic() {
        return thumbnailPic;
    }

    public void setThumbnailPic(String thumbnailPic) {
        this.thumbnailPic = thumbnailPic;
    }
}
