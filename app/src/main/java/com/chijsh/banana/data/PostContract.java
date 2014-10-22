package com.chijsh.banana.data;

import android.provider.BaseColumns;

/**
 * Created by chijsh on 10/22/14.
 */
public class PostContract {
    public static final class PostEntry implements BaseColumns {
        public static final String TABLE_NAME = "post";
        public static final String COLUMN_CONTRIBUTOR = "contributor";
        public static final String COLUMN_CONTRIBUTIONS = "contribution";

    }
}
