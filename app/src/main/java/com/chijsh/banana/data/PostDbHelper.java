package com.chijsh.banana.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.chijsh.banana.data.PostContract.PostEntry;
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

        final String SQL_CREATE_POST_TABLE = "CREATE TABLE " + PostEntry.TABLE_NAME + " (" +
                PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                PostEntry.COLUMN_CONTRIBUTOR + " TEXT NOT NULL, " +
                PostEntry.COLUMN_CONTRIBUTIONS + " INTEGER NOT NULL" + " );";

        db.execSQL(SQL_CREATE_POST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PostEntry.TABLE_NAME);
        onCreate(db);
    }
}
