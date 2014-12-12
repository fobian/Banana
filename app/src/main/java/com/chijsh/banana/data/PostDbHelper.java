package com.chijsh.banana.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.chijsh.banana.data.PostContract.PostEntry;
import com.chijsh.banana.data.PostContract.UserEntry;
import com.chijsh.banana.data.PostContract.AccountEntry;
/**
 * Created by chijsh on 10/22/14.
 */
public class PostDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    public static final String NAME = "post.db";

    public PostDbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +

                UserEntry._ID + " INTEGER PRIMARY KEY," +
                UserEntry.COLUMN_USER_ID + " TEXT UNIQUE NOT NULL, " +
                UserEntry.COLUMN_SCREEN_NAME + " TEXT NOT NULL, " +
                UserEntry.COLUMN_PROVINCE + " TEXT, " +
                UserEntry.COLUMN_CITY + " TEXT, " +
                UserEntry.COLUMN_LOCATION + " TEXT, " +
                UserEntry.COLUMN_AVATAR_SMALL + " TEXT, " +
                UserEntry.COLUMN_DESCRIPTION + " TEXT, " +
                UserEntry.COLUMN_URL + " TEXT, " +
                UserEntry.COLUMN_PROFILE_URL + " TEXT, " +
                UserEntry.COLUMN_GENDER + " TEXT, " +
                UserEntry.COLUMN_FOLLOWERS_COUNT + " INTEGER NOT NULL, " +
                UserEntry.COLUMN_FRIENDS_COUNT + " INTEGER NOT NULL, " +
                UserEntry.COLUMN_STATUSES_COUNT + " INTEGER NOT NULL, " +
                UserEntry.COLUMN_FAVOURITES_COUNT + " INTEGER NOT NULL, " +
                UserEntry.COLUMN_CREATED_AT + " TEXT NOT NULL, " +
                UserEntry.COLUMN_FOLLOWING + " INTEGER NOT NULL, " +
                UserEntry.COLUMN_AVATAR_LARGE + " TEXT, " +
                UserEntry.COLUMN_FOLLOW_ME + " INTEGER NOT NULL, " +

                "UNIQUE (" + UserEntry.COLUMN_USER_ID +") ON CONFLICT IGNORE"+
                " );";


        final String SQL_CREATE_POST_TABLE = "CREATE TABLE " + PostEntry.TABLE_NAME + " (" +

                PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PostEntry.COLUMN_CREATED_AT + " TEXT NOT NULL, " +
                PostEntry.COLUMN_POST_ID + " TEXT NOT NULL, " +
                PostEntry.COLUMN_POST_TEXT + " TEXT NOT NULL, " +
                PostEntry.COLUMN_POST_SOURCE + " TEXT NOT NULL, " +
                PostEntry.COLUMN_POST_FAVORITED + " INTEGER NOT NULL, " +
                PostEntry.COLUMN_POST_PICURLS + " TEXT, " +
                PostEntry.COLUMN_POST_GEO + " TEXT, " +
                PostEntry.COLUMN_USER_ID + " TEXT NOT NULL, " +
                PostEntry.COLUMN_USER_SCREENNAME + " TEXT NOT NULL, " +
                PostEntry.COLUMN_USER_AVATAR + " TEXT NOT NULL, " +
                PostEntry.COLUMN_RETWEETED_ID + " TEXT, " +
                PostEntry.COLUMN_RETWEETED_USER_SCREENNAME + " TEXT, " +
                PostEntry.COLUMN_RETWEETED_TEXT + " TEXT, " +
                PostEntry.COLUMN_RETWEETED_PICURLS + " TEXT, " +
                PostEntry.COLUMN_REPOST_COUNT + " INTEGER NOT NULL, " +
                PostEntry.COLUMN_COMMENT_COUNT + " INTEGER NOT NULL, " +
                PostEntry.COLUMN_ATTITUDE_COUNT + " INTEGER NOT NULL, " +

                " FOREIGN KEY (" + PostEntry.COLUMN_USER_ID + ") REFERENCES " +
                UserEntry.TABLE_NAME + " (" + UserEntry.COLUMN_USER_ID + "), " +

                "UNIQUE (" + PostEntry.COLUMN_POST_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_ACCOUNT_TABLE = "CREATE TABLE " + AccountEntry.TABLE_NAME + " (" +

                AccountEntry._ID + " INTEGER PRIMARY KEY," +
                AccountEntry.COLUMN_USER_ID + " TEXT UNIQUE NOT NULL, " +
                AccountEntry.COLUMN_SCREEN_NAME + " TEXT NOT NULL, " +
                AccountEntry.COLUMN_PROVINCE + " TEXT, " +
                AccountEntry.COLUMN_CITY + " TEXT, " +
                AccountEntry.COLUMN_LOCATION + " TEXT, " +
                AccountEntry.COLUMN_AVATAR_SMALL + " TEXT, " +
                AccountEntry.COLUMN_DESCRIPTION + " TEXT, " +
                AccountEntry.COLUMN_URL + " TEXT, " +
                AccountEntry.COLUMN_PROFILE_URL + " TEXT, " +
                AccountEntry.COLUMN_GENDER + " TEXT, " +
                AccountEntry.COLUMN_FOLLOWERS_COUNT + " INTEGER NOT NULL, " +
                AccountEntry.COLUMN_FRIENDS_COUNT + " INTEGER NOT NULL, " +
                AccountEntry.COLUMN_STATUSES_COUNT + " INTEGER NOT NULL, " +
                AccountEntry.COLUMN_FAVOURITES_COUNT + " INTEGER NOT NULL, " +
                AccountEntry.COLUMN_CREATED_AT + " TEXT NOT NULL, " +
                AccountEntry.COLUMN_FOLLOWING + " INTEGER NOT NULL, " +
                AccountEntry.COLUMN_AVATAR_LARGE + " TEXT, " +
                AccountEntry.COLUMN_FOLLOW_ME + " INTEGER NOT NULL, " +

                "UNIQUE (" + AccountEntry.COLUMN_USER_ID +") ON CONFLICT IGNORE"+
                " );";

        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_POST_TABLE);
        db.execSQL(SQL_CREATE_ACCOUNT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PostEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AccountEntry.TABLE_NAME);
        onCreate(db);
    }
}
