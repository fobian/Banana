package com.chijsh.banana.presentation.model.mapper;

import com.chijsh.banana.domain.User;
import com.chijsh.banana.presentation.model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chijsh on 3/4/15.
 */
public class UserModelDataMapper {

    public static UserModel transform(User user) {
        UserModel userModel = null;
        if (user != null) {
            userModel = new UserModel();
            user.setIdstr(user.getIdstr());
            user.setScreenName(user.getScreenName());
            user.setProvince(user.getProvince());
            user.setCity(user.getCity());
            user.setLocation(user.getLocation());
            user.setDescription(user.getDescription());
            user.setUrl(user.getUrl());
            user.setProfileImageUrl(user.getProfileImageUrl());
            user.setGender(user.getGender());
            user.setFollowersCount(user.getFollowersCount());
            user.setFriendsCount(user.getFriendsCount());
            user.setStatusesCount(user.getStatusesCount());
            user.setFavouritesCount(user.getFavouritesCount());
            user.setCreatedAt(user.getCreatedAt());
            user.setFollowing(user.isFollowing());
            user.setAvatarLarge(user.getAvatarLarge());
            user.setFollowMe(user.isFollowMe());
        }

        return userModel;
    }

    public static List<UserModel> transform(List<User> userList) {
        List<UserModel> userModels = new ArrayList<>(userList.size());
        UserModel userModel;
        for (User user : userList) {
            userModel = transform(user);
            if (userModel != null) {
                userModels.add(userModel);
            }
        }

        return userModels;
    }
}
