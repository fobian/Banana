package com.chijsh.banana.presentation.model;

/**
 * Created by chijsh on 10/20/14.
 */
public class UserModel {
    public String idstr;
    public String screenName;
    public String province;
    public String city;
    public String location;
    public String description;
    public String url;
    public String profileImageUrl;
    public String gender;
    public int followersCount;
    public int friendsCount;
    public int statusesCount;
    public int favouritesCount;
    public String createdAt;
    public boolean following;
    public String avatarLarge;
    public boolean followMe;

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if(object instanceof UserModel) {
            UserModel other = (UserModel)object;
            if(idstr.equals(other.idstr)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return idstr.hashCode();
    }
}
