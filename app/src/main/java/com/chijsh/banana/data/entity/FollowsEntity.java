package com.chijsh.banana.data.entity;

import java.util.List;

/**
 * Created by chijsh on 3/3/15.
 */
public class FollowsEntity {

    public List<UserEntity> users;
    public int nextCursor;
    public int previousCursor;
    public int totalNumber;

}
