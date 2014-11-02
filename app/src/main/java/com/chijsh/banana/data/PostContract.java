package com.chijsh.banana.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chijsh on 10/22/14.
 */
public class PostContract {

    public static final String CONTENT_AUTHORITY = "com.chijsh.banana.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_POST = "post";
    public static final String PATH_USER = "user";

    public static final class PostEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POST).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_POST;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_POST;

        public static final String TABLE_NAME = "post";
        public static final String COLUMN_CREATED_AT = "created_at";

        public static final String COLUMN_POST_ID = "idstr";
        public static final String COLUMN_POST_TEXT = "text";
        public static final String COLUMN_POST_SOURCE = "source";
        public static final String COLUMN_POST_FAVORITED = "favorited";
        public static final String COLUMN_POST_PICURLS = "pic_urls";
        public static final String COLUMN_POST_GEO = "geo";

        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_USER_SCREENNAME = "user_screenname";
        public static final String COLUMN_USER_AVATAR = "user_avatar";

        public static final String COLUMN_RETWEETED_ID = "retweeted_id";
        public static final String COLUMN_RETWEETED_USER_SCREENNAME = "retweeted_user_screenname";
        public static final String COLUMN_RETWEETED_TEXT = "retweeted_text";
        public static final String COLUMN_RETWEETED_PICURLS = "retweeted_pics";

        public static final String COLUMN_REPOST_COUNT = "repost_count";
        public static final String COLUMN_COMMENT_COUNT = "comment_count";
        public static final String COLUMN_ATTITUDE_COUNT = "attitude_count";

        public static Uri buildPostUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME = "user";

        public static final String COLUMN_USER_ID = "idstr";
        public static final String COLUMN_SCREEN_NAME = "screen_name";

        public static final String COLUMN_PROVINCE = "province";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_LOCATION = "location";

        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_PROFILE_URL = "profile_url";
        public static final String COLUMN_DOMAIN = "domain";
        public static final String COLUMN_GENDER = "gender";

        public static final String COLUMN_FOLLOWERS_COUNT= "followers_count";
        public static final String COLUMN_FRIENDS_COUNT= "friends_count";
        public static final String COLUMN_STATUSES_COUNT= "statuses_count";
        public static final String COLUMN_FAVOURITES_COUNT= "favourites_count";

        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_AVATAR_LARGE = "avatar_large";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
