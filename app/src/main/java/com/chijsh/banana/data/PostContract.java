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

    public static final class PostEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POST).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_POST;

        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_POST;

        public static final String TABLE_NAME = "post";
        public static final String COLUMN_CONTRIBUTOR = "contributor";
        public static final String COLUMN_CONTRIBUTIONS = "contribution";

        public static Uri buildPostUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildPostContributor(String contributor) {
            return CONTENT_URI.buildUpon().appendPath(contributor).build();
        }

        public static String getContributorFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

    }
}
