package com.chijsh.banana.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by chijsh on 10/13/14.
 */
public class PostProvider extends ContentProvider {

    private static final int POST = 100;
    private static final int POST_ID = 101;
    private static final int POST_BY_USER = 102;

    private static final int USER = 200;
    private static final int USER_ID = 201;

    private static final SQLiteQueryBuilder sPostByUserIdQueryBuilder;

    static {
        sPostByUserIdQueryBuilder = new SQLiteQueryBuilder();
        sPostByUserIdQueryBuilder.setTables(
                PostContract.PostEntry.TABLE_NAME + " INNER JOIN " +
                        PostContract.UserEntry.TABLE_NAME +
                        " ON " + PostContract.PostEntry.TABLE_NAME +
                        "." + PostContract.PostEntry.COLUMN_USER_ID +
                        " = " + PostContract.UserEntry.TABLE_NAME +
                        "." + PostContract.UserEntry.COLUMN_USER_ID);
    }

    private static final String sUserIdSelection =
            PostContract.UserEntry.TABLE_NAME+
                    "." + PostContract.UserEntry.COLUMN_USER_ID + " = ? ";

    private Cursor getPostByUserId(Uri uri, String[] projection, String sortOrder) {

        String userId = PostContract.PostEntry.getUserIdFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sUserIdSelection;
        selectionArgs = new String[]{userId};


        return sPostByUserIdQueryBuilder.query(mDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PostContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, PostContract.PATH_POST, POST);
        matcher.addURI(authority, PostContract.PATH_POST + "/#", POST_ID);
        matcher.addURI(authority, PostContract.PATH_POST + "/*", POST_BY_USER);

        matcher.addURI(authority, PostContract.PATH_USER, USER);
        matcher.addURI(authority, PostContract.PATH_USER + "/#", USER_ID);
        return matcher;
    }

    private PostDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new PostDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int type = sUriMatcher.match(uri);
        switch (type) {
            case POST:
                return PostContract.PostEntry.CONTENT_TYPE;
            case POST_ID:
                return PostContract.PostEntry.CONTENT_ITEM_TYPE;
            case POST_BY_USER:
                return PostContract.PostEntry.CONTENT_TYPE;
            case USER:
                return PostContract.UserEntry.CONTENT_TYPE;
            case USER_ID:
                return PostContract.UserEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case POST: {
                long _id = db.insert(PostContract.PostEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = PostContract.PostEntry.buildPostUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case USER: {
                long _id = db.insert(PostContract.UserEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = PostContract.UserEntry.buildUserUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        notifyChange(uri);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        switch (match) {
            case POST:
                rowsDeleted = db.delete(
                        PostContract.PostEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case USER:
                rowsDeleted = db.delete(
                        PostContract.UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            notifyChange(uri);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case POST:
                rowsUpdated = db.update(PostContract.PostEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case USER:
                rowsUpdated = db.update(PostContract.UserEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            notifyChange(uri);
        }
        return rowsUpdated;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case POST: {
                retCursor = mDbHelper.getReadableDatabase().query(
                        PostContract.PostEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case POST_ID: {
                retCursor = mDbHelper.getReadableDatabase().query(
                        PostContract.PostEntry.TABLE_NAME,
                        projection,
                        PostContract.PostEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case POST_BY_USER: {
                retCursor = getPostByUserId(uri, projection, sortOrder);
                break;
            }
            case USER: {
                retCursor = mDbHelper.getReadableDatabase().query(
                        PostContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case USER_ID: {
                retCursor = mDbHelper.getReadableDatabase().query(
                        PostContract.UserEntry.TABLE_NAME,
                        projection,
                        PostContract.UserEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case POST:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(PostContract.PostEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                notifyChange(uri);
                return returnCount;
            case USER:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(PostContract.UserEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                notifyChange(uri);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }
}
