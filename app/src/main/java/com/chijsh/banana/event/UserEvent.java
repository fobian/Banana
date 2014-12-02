package com.chijsh.banana.event;

import com.chijsh.banana.model.User;

import java.util.List;

/**
 * Created by chijsh on 12/2/14.
 */
public class UserEvent {
    private List<User> mUsers;
    private boolean mIsFollows;

    public UserEvent(List<User> users, boolean isFollows) {
        mUsers = users;
        mIsFollows = isFollows;
    }

    public List<User> getUsers() {
        return mUsers;
    }

    public boolean isFollows() {
        return mIsFollows;
    }
}
