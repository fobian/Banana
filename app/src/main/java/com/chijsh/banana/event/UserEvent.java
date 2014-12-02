package com.chijsh.banana.event;

import com.chijsh.banana.model.User;

import java.util.List;

/**
 * Created by chijsh on 12/2/14.
 */
public class UserEvent {
    private List<User> mUsers;

    public UserEvent(List<User> users) {
        mUsers = users;
    }

    public List<User> getUsers() {
        return mUsers;
    }
}
