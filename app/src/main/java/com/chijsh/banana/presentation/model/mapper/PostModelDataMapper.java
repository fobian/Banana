package com.chijsh.banana.presentation.model.mapper;

import com.chijsh.banana.domain.PicUrl;
import com.chijsh.banana.domain.Post;
import com.chijsh.banana.presentation.model.PicUrlModel;
import com.chijsh.banana.presentation.model.PostModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chijsh on 3/4/15.
 */
public class PostModelDataMapper {

    public static PostModel transform(Post post) {
        PostModel postModel = null;
        if (post != null) {
            postModel = new PostModel();
            postModel.setIdstr(post.getIdstr());
            postModel.setCreatedAt(post.getCreatedAt());
            postModel.setText(post.getText());
            postModel.setSource(post.getSource());
            postModel.setFavorited(post.isFavorited());
            postModel.setPicUrls(transformPicUrl(post.getPicUrls()));
            postModel.setUser(UserModelDataMapper.transform(post.getUser()));
            postModel.setRetweetedStatus(transform(post.getRetweetedStatus()));
        }

        return postModel;
    }

    public static List<PostModel> transform(List<Post> postList) {
        List<PostModel> postModels = new ArrayList<>(postList.size());
        PostModel postModel;
        for (Post post : postList) {
            postModel = transform(post);
            if (postModel != null) {
                postModels.add(postModel);
            }
        }

        return postModels;
    }

    public static List<PicUrlModel> transformPicUrl(List<PicUrl> picUrlList) {
        List<PicUrlModel> picUrlModels = new ArrayList<>(picUrlList.size());
        PicUrlModel picUrlModel;
        for (PicUrl picUrl : picUrlList) {
            picUrlModel = transform(picUrl);
            if (picUrlModel != null) {
                picUrlModels.add(picUrlModel);
            }
        }
        return picUrlModels;
    }

    public static PicUrlModel transform(PicUrl picUrl) {
        PicUrlModel picUrlModel = null;
        if (picUrl != null) {
            picUrlModel = new PicUrlModel();
            picUrlModel.setThumbnailPic(picUrl.getThumbnailPic());
        }
        return picUrlModel;
    }

}
