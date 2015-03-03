package com.chijsh.banana.data.entity.mapper;

import com.chijsh.banana.data.entity.GeoEntity;
import com.chijsh.banana.data.entity.PicUrlEntity;
import com.chijsh.banana.data.entity.PostEntity;
import com.chijsh.banana.domain.Geo;
import com.chijsh.banana.domain.PicUrl;
import com.chijsh.banana.domain.Post;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by chijsh on 3/3/15.
 */
public class PostEntityDataMapper {

    public static Post transform(PostEntity postEntity) {
        Post post = null;
        if (postEntity != null) {
            post = new Post();
            post.setIdstr(postEntity.getIdstr());
            post.setCreatedAt(postEntity.getCreatedAt());
            post.setText(postEntity.getText());
            post.setSource(postEntity.getSource());
            post.setFavorited(postEntity.isFavorited());
            post.setPicUrls(transform(postEntity.getPicUrls()));
            post.setGeo(transform(postEntity.getGeo()));
            post.setUser(UserEntityDataMapper.transform(postEntity.getUser()));
            post.setRetweetedStatus(transform(postEntity.getRetweetedStatus()));
        }

        return post;
    }

    public static List<Post> transform(List<PostEntity> postEntityList) {
        List<Post> postList = new ArrayList<>(postEntityList.size());
        Post post;
        for (PostEntity postEntity : postEntityList) {
            post = transform(postEntity);
            if (post != null) {
                postList.add(post);
            }
        }

        return postList;
    }

    public static List<PicUrl> transform(RealmList<PicUrlEntity> picUrlEntityRealmList) {
        List<PicUrl> picUrlList = new ArrayList<>(picUrlEntityRealmList.size());
        PicUrl picUrl;
        for (PicUrlEntity picUrlEntity : picUrlEntityRealmList) {
            picUrl = transform(picUrlEntity);
            if (picUrl != null) {
                picUrlList.add(picUrl);
            }
        }
        return picUrlList;
    }

    public static PicUrl transform(PicUrlEntity picUrlEntity) {
        PicUrl picUrl = null;
        if (picUrlEntity != null) {
            picUrl = new PicUrl();
            picUrl.setThumbnailPic(picUrlEntity.getThumbnailPic());
        }
        return picUrl;
    }

    public static Geo transform(GeoEntity geoEntity) {
        Geo geo = null;
        if (geoEntity != null) {
            geo = new Geo();
            geo.setType(geoEntity.getType());
            geo.setCoordinates(geoEntity.getCoordinates());
        }
        return geo;
    }
}
