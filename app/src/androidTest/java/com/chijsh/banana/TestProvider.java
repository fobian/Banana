package com.chijsh.banana;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.chijsh.banana.data.PostContract.PostEntry;
import com.chijsh.banana.data.PostDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by chijsh on 10/22/14.
 */
public class TestProvider extends AndroidTestCase {

    private static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecords() {
        mContext.getContentResolver().delete(PostEntry.CONTENT_URI, null, null);
        Cursor cursor = mContext.getContentResolver().query(
                PostEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

    }
    public void testInsertProvider() {

        ContentValues values = TestDb.createValues();

        Uri insertUri = mContext.getContentResolver().insert(PostEntry.CONTENT_URI, values);
        assertTrue(insertUri != null);

        Cursor cursor = mContext.getContentResolver().query(
                PostEntry.CONTENT_URI,  // Table to Query
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null // sort order
        );

        TestDb.validateCursor(cursor, values);
    }

    public void testUpdatePost() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestDb.createValues();

        Uri postUri = mContext.getContentResolver().
                insert(PostEntry.CONTENT_URI, values);
        long postRowId = ContentUris.parseId(postUri);

        // Verify we got a row back.
        assertTrue(postRowId != -1);
        Log.d(LOG_TAG, "New row id: " + postRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(PostEntry._ID, postRowId);
        updatedValues.put(PostEntry.COLUMN_CONTRIBUTOR, "Jason Chi");

        int count = mContext.getContentResolver().update(
                PostEntry.CONTENT_URI, updatedValues, PostEntry._ID + "= ?",
                new String[] { Long.toString(postRowId)});

        assertEquals(count, 1);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                PostEntry.CONTENT_URI,
                null,
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null // sort order
        );

        TestDb.validateCursor(cursor, updatedValues);
    }

    public void setUp() {
        deleteAllRecords();
    }

    public void testDeleteRecordsAtEnd() {
        deleteAllRecords();
    }
}
