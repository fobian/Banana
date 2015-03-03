package com.chijsh.banana.data.entity.mapper;

import com.chijsh.banana.data.entity.UserEntity;
import com.chijsh.banana.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chijsh on 3/3/15.
 */
public class UserEntityDataMapper {

    public static User transform(UserEntity userEntity) {
        User user = null;
        if (userEntity != null) {
            user = new User();
            user.setIdstr(userEntity.getIdstr());
            user.setScreenName(userEntity.getScreenName());
            user.setProvince(userEntity.getProvince());
            user.setCity(userEntity.getCity());
            user.setLocation(userEntity.getLocation());
            user.setDescription(userEntity.getDescription());
            user.setUrl(userEntity.getUrl());
            user.setProfileImageUrl(userEntity.getProfileImageUrl());
            user.setGender(userEntity.getGender());
            user.setFollowersCount(userEntity.getFollowersCount());
            user.setFriendsCount(userEntity.getFriendsCount());
            user.setStatusesCount(userEntity.getStatusesCount());
            user.setFavouritesCount(userEntity.getFavouritesCount());
            user.setCreatedAt(userEntity.getCreatedAt());
            user.setFollowing(userEntity.isFollowing());
            user.setAvatarLarge(userEntity.getAvatarLarge());
            user.setFollowMe(userEntity.isFollowMe());
        }

        return user;
    }

    public static List<User> transform(List<UserEntity> userEntityList) {
        List<User> userList = new ArrayList<>(userEntityList.size());
        User user;
        for (UserEntity userEntity : userEntityList) {
            user = transform(userEntity);
            if (user != null) {
                userList.add(user);
            }
        }

        return userList;
    }
}
