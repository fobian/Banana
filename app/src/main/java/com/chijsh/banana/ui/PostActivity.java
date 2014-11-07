package com.chijsh.banana.ui;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chijsh.banana.AccessTokenKeeper;
import com.chijsh.banana.R;
import com.chijsh.banana.data.PostContract.AccountEntry;
import com.chijsh.banana.widget.BezelImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PostActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int USER_LOADER = 0;
    public static final int PICK_OR_TAKE_PICTURE = 42;

    @InjectView(R.id.toolbar_actionbar) Toolbar toolbar;
    @InjectView(R.id.my_avatar) BezelImageView mAvatar;
    @InjectView(R.id.my_name) TextView mNameTextView;

    @InjectView(R.id.post_edit) AutoCompleteTextView mPostEdit;

    @InjectView(R.id.post_camera) ImageButton mCameraAction;
    @InjectView(R.id.post_at) ImageButton mAtAction;
    @InjectView(R.id.post_emotion) ImageButton mEmotionAction;

    @InjectView(R.id.post_send) ImageButton mSendAction;

    private static final String[] USER_COLUMNS = {
            AccountEntry.TABLE_NAME + "." + AccountEntry._ID,
            AccountEntry.COLUMN_USER_ID,
            AccountEntry.COLUMN_SCREEN_NAME,
            AccountEntry.COLUMN_PROVINCE,
            AccountEntry.COLUMN_CITY,
            AccountEntry.COLUMN_LOCATION,
            AccountEntry.COLUMN_DESCRIPTION,
            AccountEntry.COLUMN_URL,
            AccountEntry.COLUMN_PROFILE_URL,
            AccountEntry.COLUMN_GENDER,
            AccountEntry.COLUMN_FOLLOWERS_COUNT,
            AccountEntry.COLUMN_FRIENDS_COUNT,
            AccountEntry.COLUMN_STATUSES_COUNT,
            AccountEntry.COLUMN_FAVOURITES_COUNT,
            AccountEntry.COLUMN_CREATED_AT,
            AccountEntry.COLUMN_FOLLOWING,
            AccountEntry.COLUMN_AVATAR_LARGE,
            AccountEntry.COLUMN_FOLLOW_ME,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.inject(this);

        getLoaderManager().initLoader(USER_LOADER, null, this);
    }

    @OnClick(R.id.post_camera)
    public void addPicture() {
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickIntent.setType("image/*");

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooserIntent = Intent.createChooser(pickIntent,
                getString(R.string.title_add_picture));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                new Intent[] { takePhotoIntent });

        startActivityForResult(chooserIntent, PICK_OR_TAKE_PICTURE);
    }

    @OnClick(R.id.post_at)
    public void atSomebody() {

    }

    @OnClick(R.id.post_emotion)
    public void addEmotion() {

    }

    @OnClick(R.id.post_send)
    public void postWeibo() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = AccountEntry.CONTENT_URI;
        String selection = AccountEntry.COLUMN_USER_ID + " = '" + AccessTokenKeeper.readAccessToken(this).getUid() + "'";

        return new CursorLoader(
                this,
                uri,
                USER_COLUMNS,
                selection,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor != null && cursor.moveToFirst()) {
            String avatarUrl = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_AVATAR_LARGE));
            String name = cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_SCREEN_NAME));
            Glide.with(this)
                    .load(avatarUrl)
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.user_avatar_empty)
                    .into(mAvatar);
            mNameTextView.setText(name);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //TODO
    }
}
